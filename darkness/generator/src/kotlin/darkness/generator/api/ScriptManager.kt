package darkness.generator.api

import darkness.generator.api.effects.EffectBase
import darkness.generator.output.BaseOutput
import kotlinx.coroutines.GlobalScope
import java.util.LinkedList
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.channels.ClosedSendChannelException
import kotlinx.coroutines.launch

/**
 * Keeps track of running scripts/effects and synchronizes their frame generation.
 */
object ScriptManager {
    /** The currently active scripts. Might be modified from a coroutine, so every access to it must be synchronized. */
    private val scriptContexts = LinkedList<ScriptContext>()

    /** The sequence output. */
    private lateinit var output: BaseOutput

    /** The index of the current frame. */
    private var frameCount = 0

    /**
     * Starts the [mainScript] of a sequence, and runs it and its sub-scripts
     * and effects to completion, writing the resulting frames to [output].
     */
    suspend fun start(mainScript: ScriptBase, output: BaseOutput) {
        this.output = output
        var currentFrame = Frame(ChannelManager.createBlankFrame())
        // Start the main script
        registerScript(mainScript)

        while (true) {
            // If there are no active scripts/effects, we're done
            if (synchronized(scriptContexts) { scriptContexts.isEmpty() }) {
                break
            }

            // We make a copy of the script context list in order to avoid `ConcurrentModificationException`
            // (in case the script itself adds another script, and because we will remove the script if it's finished).
            // Copying also makes sure that we won't start processing sub-scripts
            // and effects that are added in this frame until the next frame starts.
            // It is particularly important that both loops below see the same list - if the second loop
            // sees scripts that are added by the first one, a deadlock will result (we will wait for the script
            // to send a frame to us, but it won't do that until we have sent a frame to it).
            val scriptContextsCopy = synchronized(scriptContexts) { scriptContexts.toList() }

            // Send the current frame to every script/effect, which will make each of them generate a single frame.
            // Each one receives their own `MutableFrame`, so that they won't step on each other's toes.
            // Doing this in a separate loop lets them execute in parallel instead of us waiting
            // for each one to generate the next frame before asking the next one to do so.
            for (scriptContext in scriptContextsCopy) {
                try {
                    scriptContext.coroutineChannel.send(MutableFrame(currentFrame))
                } catch (e: ClosedSendChannelException) {
                    // When a script/effect is cancelled, it will close its coroutine channel,
                    // which will cause an exception to be thrown when we try to send to the channel.
                    // At that point, we remove the script/effect.
                    removeScriptContext(scriptContext)
                }
            }

            // Collect the resulting frame from each script/effect, and overlay them on top of each other.
            // We look at the scripts/effects in the order they were added, and if a later script/effect tries to set
            // a channel that has already been set by an earlier script/effect, their relative priorities determine which one wins.
            // `priorityOfDmxChannelSetter` keeps track of the priority of the most recent setter of each channel in this frame.
            // We collect the result from each frame by calling `receive()` on the coroutine channel, which will wait until
            // the script has computed the frame and sent it to the coroutine channel.
            // If a script doesn't execute `next()` but returns from `run()` instead, `ScriptBase` will close the channel
            // instead of sending a message. That's our signal that the script is done.
            val nextFrame = currentFrame.channelValues.toMutableMap()
            val priorityOfDmxChannelSetter = mutableMapOf<darkness.generator.api.Channel, Int>()
            for (scriptContext in scriptContextsCopy) {
                try {
                    val modifiedFrame = scriptContext.coroutineChannel.receive()
                    for ((channel, value) in modifiedFrame.getModifiedChannelValues()) {
                        val priority = scriptContext.script.priority
                        if (priority > priorityOfDmxChannelSetter.getOrDefault(channel, -1)) {
                            nextFrame[channel] = value
                            priorityOfDmxChannelSetter[channel] = priority
                        }
                    }
                } catch (e: ClosedReceiveChannelException) {
                    // When a script/effect is done, it will return from `run()`, at which point `start()` will close the communication channel,
                    // which will cause an exception to be thrown when we try to receive from the channel. At that point, we remove the script/effect.
                    removeScriptContext(scriptContext)
                    output.endScript(scriptContext.script.toString())
                }
            }

            output.beginFrame()
            currentFrame = Frame(nextFrame)
            currentFrame.dumpChannels(output)
            output.endFrame()
            frameCount++
        }

        output.flush()
    }

    /**
     * Start the given script in a coroutine. A coroutine is running on its own thread,
     * and has the ability to pause and resume its execution. This is what allows us to
     * "jump out" of a script/effect when it calls `next()` or `skip()`.
     */
    suspend fun registerScript(script: ScriptBase) {
        // Note that the `Channel` class we're using here is a coroutine channel,
        // which is unrelated to a DMX channel. A coroutine channel is
        // the mechanism we use for passing data to and from coroutines.
        val scriptCoroutineChannel = Channel<MutableFrame>()
        synchronized(scriptContexts) {
            scriptContexts.add(ScriptContext(script, scriptCoroutineChannel))
        }
        script.isRunning = true
        // `start()` will immediately wait for a message over the channel before starting to generate the first frame
        GlobalScope.launch { script.start(scriptCoroutineChannel) }
        output.beginScript(script.toString())
        println("[$frameCount] Started script/effect: $script")
    }

    /**
     * Start the given effect in a coroutine. See [registerScript].
     */
    suspend fun registerEffect(effect: EffectBase) {
        registerScript(effect)
    }

    private fun removeScriptContext(scriptContext: ScriptContext) {
        scriptContext.script.isRunning = false
        synchronized(scriptContexts) {
            scriptContexts.remove(scriptContext)
        }
    }
}

/**
 * A running script/effect and its communication channel.
 */
private class ScriptContext(
    val script: ScriptBase,
    val coroutineChannel: Channel<MutableFrame>
)
