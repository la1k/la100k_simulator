package darkness.generator.api

import darkness.generator.api.effects.EffectBase
import darkness.generator.api.effects.Hold
import darkness.generator.api.effects.RGBFade
import darkness.generator.api.effects.HSBFade
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.channels.ClosedSendChannelException

import java.awt.*

/**
 * A [ScriptBase] can be anything that controls channel values, either over time or as a one-shot action.
 * Subclasses of [EffectBase] will typically be one single effect (which may last across several frames),
 * while direct subclasses of [ScriptBase] will typically be scripts that are a collection of effects.
 * Subclasses should implement [run] as a sequence of calls to the methods in this class.
 * Each method's effect will start (and possibly end) in the current frame.
 * [next] declares that the current frame is complete, and advances to the next frame.
 * [skip] also declares that the current frame is complete, and advances ahead by the given number of frames.
 */
abstract class ScriptBase {
    /**
     * Priority of this script/effect when multiple scripts/effects want to
     * set the same channel in the same frame. Higher is better.
     */
    var priority: Int = 0

    /** Whether this script/effect has been cancelled. */
    var isCancelled: Boolean = false
        private set

    /** Status object if script is running */
    var isRunning: Boolean = false

    /** The communication channel with the [ScriptManager], which controls the progress through the frames. */
    private lateinit var coroutineChannel: Channel<MutableFrame>

    /** The frame that is currently being generated. */
    protected lateinit var currentFrame: MutableFrame
        private set

    /** Convenience method for accessing the bulb with the given [id]. */
    protected fun bulb(id: Int): BulbRGB {
        return BulbManager.getBulb(id)
    }

    /** Convenience method for creating a [BulbGroup] for accessing all the bulbs with the ids given by [ids]. */
    protected fun group(vararg ids: Int): BulbGroup {
        val bulbs = ids.map(::bulb)
        return BulbGroup(bulbs)
    }

    /** Starts the script/effect. [coroutineChannel] is the communication channel with the [ScriptManager]. */
    suspend fun start(coroutineChannel: Channel<MutableFrame>) {
        this.coroutineChannel = coroutineChannel
        this.currentFrame = this.coroutineChannel.receive()
        run()
        this.coroutineChannel.close()
    }

    /** The run function of the script is responsible for generating the sequence. */
    abstract suspend fun run()

    /** Stop the script/effect. */
    open fun cancel() {
        isCancelled = true
        coroutineChannel.close()
    }

    /*************************************************
     * Sequence functions below
     */

    /**
     * Declares the current frame in this script or effect to be finished, sends the result
     * to [ScriptManager], and suspends ("pauses") this function. The suspension will last until all the
     * concurrent scripts/effects are done with generating their current frame and [ScriptManager]
     * has merged all the frames. Afterwards,
     */
    protected suspend fun next() {
        try {
            // Send our result to the script manager
            coroutineChannel.send(currentFrame)
            // Wait for the script manager to send us the next frame
            currentFrame = coroutineChannel.receive()
        } catch (e: Exception) {
            // This is hopefully because we've been cancelled, in which case we just return
            // and hope that the script's `run()` function knows to exit when cancelled
            if (!(e is ClosedSendChannelException || e is ClosedReceiveChannelException) || !isCancelled) {
                throw e
            }
        }
    }

    /**
     * Declares the current frame in this script or effect to be finished,
     * and then skips the given number of frames. Any effects or sub-scripts
     * that might have been started by this script/effect will keep executing
     * during those frames. This is equivalent to calling [next] the given number of times.
     * @param frames The number of frames to wait.
     */
    protected suspend fun skip(frames: Int) {
        var i = 0
        while (i < frames && !isCancelled) {
            next()
            i++
        }
    }

    /** Runs an effect in parallel to the current script. */
    protected suspend fun effect(effect: EffectBase) {
        ScriptManager.registerEffect(effect)
    }

    /** Runs another script in parallel to the current script. */
    protected suspend fun merge(script: ScriptBase) {
        ScriptManager.registerScript(script)
    }

    /**
     * Member extension function that can be invoked on a bulb. It will return the value
     * of the red channel of that bulb in the frame that is currently being generated.
     */
    protected val BulbSet.red get() = redIn(currentFrame)

    /**
     * Member extension function that can be invoked on a bulb. It will return the value
     * of the green channel of that bulb in the frame that is currently being generated.
     */
    protected val BulbSet.green get() = greenIn(currentFrame)

    /**
     * Member extension function that can be invoked on a bulb. It will return the value
     * of the blue channel of that bulb in the frame that is currently being generated.
     */
    protected val BulbSet.blue get() = blueIn(currentFrame)

    /**
     * Member extension function that can be invoked on a bulb. It will return
     * the color of that bulb in the frame that is currently being generated.
     */
    protected val BulbSet.color get() = Color(red, green, blue)

    /**
     * Member extension function that can be invoked on a bulb. It will return the value
     * of the red channel of that bulb in the frame that is currently being generated.
     */
    protected val darkness.generator.api.Channel.value get() = currentFrame.getValue(this)

    /**
     * Sets the color of the given [bulbSet].
     * @param bulbSet The bulb or bulb group to set the color on
     * @param red Value between 0..255
     * @param green Value between 0..255
     * @param blue Value between 0..255
     */
    protected fun set(bulbSet: BulbSet, red: Int, green: Int, blue: Int) {
        bulbSet.set(red, green, blue, currentFrame)
    }

    /** Sets the color of the given [bulbSet] in the current frame. */
    protected fun set(bulbSet: BulbSet, color: Color) {
        bulbSet.set(color, currentFrame)
    }

    /**
     * Sets the HSB color of the given [bulbSet] in the current frame.
     * @param bulbSet The bulb or bulb group to set the color on
     * @param hue The floor of this number is subtracted from it to create a fraction between 0 and 1. This fractional number is then multiplied by 360 to produce the hue angle in the HSB color model.
     * @param saturation In the range 0.0..1.0
     * @param brightness In the range 0.0..1.0
     */
    protected fun setHSB(bulbSet: BulbSet, hue: Float, saturation: Float, brightness: Float) {
        bulbSet.setHSB(hue, saturation, brightness, currentFrame)
    }

    /**
     * Sets the color of the given [bulbSet] in the current frame. Out-of-range values will be coerced to [0-255].
     */
    protected fun setCoerced(bulbSet: BulbSet, red: Double, green: Double, blue: Double) {
        bulbSet.setCoerced(red, green, blue, currentFrame)
    }

    /**
     * Can be used from the main script to override the color of a bulb or bulb group which would otherwise be set by a subscript or effect.
     * As opposed to [set], its effect can last for many frames.
     */
    protected suspend fun hold(bulbSet: BulbSet, color: Color, frames: Int) {
        effect(Hold(bulbSet, color, frames))
    }

    /**
     * Can be used from the main script to override the color of a bulb or bulb group which would otherwise be set by a subscript or effect.
     * As opposed to [set], its effect can last for many frames.
     */
    protected suspend fun hold(bulbSet: BulbSet, red: Int, green: Int, blue: Int, frames: Int) {
        hold(bulbSet, Color(red, green, blue), frames)
    }

    /**
     * Can be used from the main script to override the color of a bulb or bulb group which would otherwise be set by a subscript or effect.
     * As opposed to [setHSB], its effect can last for many frames.
     */
    protected suspend fun holdHSB(bulbSet: BulbSet, hue: Float, saturation: Float, brightness: Float, frames: Int) {
        hold(bulbSet, Color.getHSBColor(hue, saturation, brightness), frames)
    }

    /**
     * Starts a new [RGBFade] effect on the given bulb(s), starting with the current color
     * of the bulb(s) (if more than one bulb, only the color of the first one will matter).
     */
    protected suspend fun rgbFade(bulbSet: BulbSet, toColor: Color, duration: Int) {
        rgbFade(bulbSet, bulbSet.color, toColor, duration)
    }

    /** Starts a new [RGBFade] effect on the given bulb(s). */
    protected suspend fun rgbFade(bulbSet: BulbSet, fromColor: Color, toColor: Color, duration: Int) {
        effect(RGBFade(bulbSet, fromColor, toColor, duration))
    }

    /** Starts a new [HSBFade] effect on the given bulb(s). */
    protected suspend fun hsbFade(bulbSet: BulbSet, color: FloatArray, duration: Int) {
        effect(HSBFade(bulbSet, color, duration))
    }

    /** Starts a new [HSBFade] effect on the given bulb(s). */
    protected suspend fun hsbFade(bulbSet: BulbSet, hue: Float, saturation: Float, brightness: Float, duration: Int) {
        hsbFade(bulbSet, floatArrayOf(hue, saturation, brightness), duration)
    }

    /** Starts a new [RGBFade] effect on the given bulb(s). */
    protected suspend fun rgbFade(bulbSet: BulbSet, red: Int, green: Int, blue: Int, duration: Int) {
        rgbFade(bulbSet, bulbSet.color, Color(red, green, blue), duration)
    }

    /** Legacy function that no longer does anything. */
    protected fun relinquish(bulbSet: BulbSet) {
    }
}
