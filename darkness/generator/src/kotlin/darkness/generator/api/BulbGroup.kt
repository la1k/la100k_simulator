package darkness.generator.api

import java.awt.Color
import java.lang.IllegalArgumentException

/**
 * A group of one or more bulbs, that can mostly be treated as one "big" bulb through the [BulbSet] interface.
 *
 * Constructor creates a bulb group that consists of the given bulbs.
 */
class BulbGroup(val allBulbs: List<BulbRGB>) : BulbSet {
    val numBulbs: Int

    constructor(vararg bulbs: BulbRGB) : this(bulbs.toList())

    override val position: FloatArray
        get() {
            val averagePosition = floatArrayOf(0.0f, 0.0f, 0.0f)
            var count = 0
            for (bulb in allBulbs) {
                val pos = bulb.position
                averagePosition[0] += pos[0]
                averagePosition[1] += pos[1]
                averagePosition[2] += pos[2]
                count++
            }

            averagePosition[0] /= count.toFloat()
            averagePosition[1] /= count.toFloat()
            averagePosition[2] /= count.toFloat()
            return averagePosition

        }

    init {
        if (allBulbs.isEmpty()) {
            throw IllegalArgumentException("bulbs must be non-null and contain elements")
        }
        this.numBulbs = allBulbs.size
    }

    /**
     * Get a single bulb.
     * @param idx Bulb index
     * @return
     */
    fun getBulb(idx: Int): BulbRGB {
        return allBulbs[idx]
    }

    /** The RGB color that is indicated by the values of the first bulb's channels in the given frame. */
    override fun colorIn(frame: Frame) = Color(redIn(frame), greenIn(frame), blueIn(frame))

    /** The red component that is indicated by the current values of the first bulb's red channel in the given frame. */
    override fun redIn(frame: Frame) = frame.getValue(allBulbs[0].channelRed)

    /** The green component that is indicated by the current values of the first bulb's green channel in the given frame. */
    override fun greenIn(frame: Frame) = frame.getValue(allBulbs[0].channelGreen)

    /** The blue component that is indicated by the current values of the first bulb's blue channel in the given frame. */
    override fun blueIn(frame: Frame) = frame.getValue(allBulbs[0].channelBlue)

    /** Set all bulbs in the group to the given RGB color in the given frame. */
    override fun set(red: Int, green: Int, blue: Int, frame: MutableFrame) {
        for (bulb in allBulbs) {
            bulb.set(red, green, blue, frame)
        }
    }

    /** Set all bulbs in the group to the given RGB color in the given frame. */
    override fun set(color: Color, frame: MutableFrame) {
        for (bulb in allBulbs) {
            bulb.set(color, frame)
        }
    }

    /** Set all bulbs in the group to the given HSB color in the given frame. */
    override fun setHSB(hue: Float, saturation: Float, brightness: Float, frame: MutableFrame) {
        for (bulb in allBulbs) {
            bulb.setHSB(hue, saturation, brightness, frame)
        }
    }

    /** @return A string describing the channels of the bulbs in this group. */
    override fun toString() = allBulbs.joinToString(",", "BulbSet{", "}")

    /** Iterate over all the bulbs in this group. */
    override fun iterator(): Iterator<BulbRGB> {
        return allBulbs.iterator()
    }
}
