package darkness.generator.api

/**
 * A class that represents a single grayscale DMX channel.
 * Multiple channels can be grouped together to bulbs, giving for example an RGB bulb.
 *
 * Constructor creates a channel object with universe and channel number.
 * @param universe The number of the DMX universe this channel is connected to
 * @param channel The DMX channel number between 1 and 512 (inclusive)
 */
data class Channel(val universe: Int, val channel: Int) {
    init {
        if (channel < 1 || channel > 512) {
            throw IllegalArgumentException("The channel must be between 1 and 512 (inclusive)")
        }
    }

    /**
     * A constructor that only gives the channel number in the DMX universe.
     * The universe is implicitly set to 0.
     * @param channel The DMX channel number between 1 and 512 (inclusive)
     */
    internal constructor(channel: Int) : this(0, channel) {}

    /**
     * Sets the value of the channel in the given frame.
     * @param value A value between 0 and 255 inclusive (to indicate a DMX channel value),
     * or 256 (to indicate transparency; only useful in overlays),
     * unless it has already been actively set to something else in the same frame).
     */
    fun setValue(value: Int, currentFrame: MutableFrame) {
        if (value < 0 || value > 256) {
            throw IllegalArgumentException("The channel value must be between 0 and 255 (inclusive), or 256 for transparency")
        }
        currentFrame.setValue(this, value)
    }

    override fun toString() = "{U:$universe,C:$channel}"
}
