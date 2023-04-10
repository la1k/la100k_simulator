package darkness.generator.api

import darkness.generator.output.BaseOutput

/**
 * Represents one "finalized", immutable DMX frame.
 * Contains the value that every DMX channel has in that frame.
 */
open class Frame(val channelValues: Map<Channel, Int>) {
    open fun getValue(channel: Channel): Int {
        return channelValues.getValue(channel)
    }

    fun dumpChannels(output: BaseOutput) {
        channelValues.forEach {(c, i) -> output.writeChannelValue(c, i)}
    }
}

/**
 * Represents a DMX frame that is under construction. Each script/effect is given its own [MutableFrame],
 * into which it can write the channel values it wishes to set in that frame.
 * After every script/effect has generated its next frame, [ScriptManager] will combine the channel
 * values from the resulting [MutableFrame] objects according to script order and priorities.
 */
class MutableFrame(
    originalFrame: Frame
): Frame(originalFrame.channelValues) {
    private val modifiedChannelValues = mutableMapOf<Channel, Int>()

    fun getModifiedChannelValues(): Map<Channel, Int> = modifiedChannelValues

    override fun getValue(channel: Channel): Int {
        return modifiedChannelValues[channel] ?: super.getValue(channel)
    }

    fun setValue(channel: Channel, value: Int) {
        modifiedChannelValues[channel] = value
    }
}
