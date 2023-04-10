package darkness.generator.api

/**
 * Keeps track of the available channels.
 */
object ChannelManager {
    // Key = Universe * 1000 + channel
    private val channelMap = mutableMapOf<Int, Channel>()

    /**
     * Gets or creates a channel with the given universe and channel number.
     * @param universe The number of the DMX universe this channel is connected to
     * @param channel The DMX channel number between 1 and 512 (inclusive)
     * @return The Channel object related to the given universe and channel number
     */
    fun getChannel(universe: Int, channel: Int): Channel {
        if (channel < 1 || channel > 512) {
            throw IllegalArgumentException("The channel must be between 1 and 512 (inclusive)")
        }

        return channelMap.computeIfAbsent(universe * 1000 + channel) { Channel(universe, channel) }
    }

    /**
     * Gets or creates a channel with channel number (the universe is implicitly set to 0).
     * @param channel The DMX channel number between 1 and 512 (inclusive)
     * @return The Channel object related to the given universe and channel number
     */
    fun getChannel(channel: Int): Channel {
        return getChannel(0, channel)
    }

    fun createBlankFrame() = channelMap.values.associateBy({ it }, { 0 }).toMutableMap()
}
