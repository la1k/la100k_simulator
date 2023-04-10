package darkness.generator.api

/**
 * Keeps track of the available bulbs.
 */
object BulbManager {
    private val bulbMap = mutableMapOf<Int, BulbRGB>()
    val allBulbs: Collection<BulbRGB>
        get() = bulbMap.values

    fun getBulb(id: Int): BulbRGB {
        return bulbMap.getOrElse(id) { throw IllegalArgumentException("There is no bulb with id $id") }
    }

    /**
     * Registers a bulb with the given integer id
     * @param id
     * @param channelRed
     * @param channelGreen
     * @param channelBlue
     * @return
     */
    fun registerBulb(id: Int, channelRed: Channel, channelGreen: Channel, channelBlue: Channel, posX: Float, posY: Float): BulbRGB {
        if (bulbMap.containsKey(id)) {
            throw IllegalArgumentException("The bulb with the ID: $id already exists.")
        }

        val bulb = BulbRGB(id, channelRed, channelGreen, channelBlue, posX, posY)
        bulbMap[id] = bulb
        return bulb
    }

    fun registerBulb(id: Int, channelRed: Channel, channelGreen: Channel, channelBlue: Channel): BulbRGB {
        if (bulbMap.containsKey(id)) {
            throw IllegalArgumentException("The bulb with the ID: $id already exists.")
        }

        return registerBulb(id, channelRed, channelGreen, channelBlue, -1.0f, -1.0f)
    }

    /**
     * Registers a bulb with the given integer id and channel numbers. The universe is implicitly set to 0.
     * @param id
     * @param channelRed
     * @param channelGreen
     * @param channelBlue
     * @return
     */
    fun registerBulb(id: Int, channelRed: Int, channelGreen: Int, channelBlue: Int): BulbRGB {
        if (bulbMap.containsKey(id)) {
            throw IllegalArgumentException("The bulb with the ID: $id already exists.")
        }
        return registerBulb(id, ChannelManager.getChannel(channelRed), ChannelManager.getChannel(channelGreen), ChannelManager.getChannel(channelBlue))
    }

    fun registerBulb(id: Int, channelRed: Int, channelGreen: Int, channelBlue: Int, posX: Float, posY: Float): BulbRGB {
        if (bulbMap.containsKey(id)) {
            throw IllegalArgumentException("The bulb with the ID: $id already exists.")
        }
        return registerBulb(id, ChannelManager.getChannel(channelRed), ChannelManager.getChannel(channelGreen), ChannelManager.getChannel(channelBlue), posX, posY)
    }
}
