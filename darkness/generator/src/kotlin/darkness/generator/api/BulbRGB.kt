package darkness.generator.api

import java.awt.Color

/**
 * An RGB bulb, whose color is defined by three channels.
 * Constructor creates a new bulb that is controlled by the given channels.
 */
class BulbRGB(
    /** The bulb id. */
    val id: Int,
    /** The channel that controls the red component of this bulb. */
    val channelRed: Channel,
    /** The channel that controls the green component of this bulb. */
    val channelGreen: Channel,
    /** The channel that controls the blue component of this bulb. */
    val channelBlue: Channel,
    /** The x position. */
    posX: Float,
    /** The y position. */
    posY: Float
) : BulbSet {
    override val position = floatArrayOf(-1.0f, -1.0f, 0.0f)

    init {
        this.position[0] = posX
        this.position[1] = posY
    }

    /** The RGB color that is indicated by the current values of this bulb's channels. */
    override fun colorIn(frame: Frame) = Color(redIn(frame), greenIn(frame), blueIn(frame))

    /** The red component that is indicated by the current values of this bulb's red channel. */
    override fun redIn(frame: Frame) = frame.getValue(channelRed)

    /** The green component that is indicated by the current values of this bulb's green channel. */
    override fun greenIn(frame: Frame) = frame.getValue(channelGreen)

    /** The blue component that is indicated by the current values of this bulb's blue channel. */
    override fun blueIn(frame: Frame) = frame.getValue(channelBlue)

    /** Set the RGB color of this bulb in the given frame, by setting the individual channels to the given values. */
    override fun set(red: Int, green: Int, blue: Int, frame: MutableFrame) {
        channelRed.setValue(red, frame)
        channelGreen.setValue(green, frame)
        channelBlue.setValue(blue, frame)
    }

    /**
     * Set the RGB color of this bulb in the given frame, by setting
     * the individual channels to the components of the given [Color].
     */
    override fun set(color: Color, frame: MutableFrame) {
        set(color.red, color.green, color.blue, frame)
    }

    /**
     * Set the RGB color of this bulb in the given frame, by setting the individual
     * channels to the components of the given hexadecimal RGB string.
     * `hexColor` must start with "0x" or "#".
     */
    operator fun set(hexColor: String, frame: MutableFrame) {
        set(Color.decode(hexColor), frame)
    }

    /**
     * Set a HSB color of this bulb in the given frame, by setting the individual
     * channels to the components of the RGB color that corresponds to the given HSB color.
     * @param hue The floor of this number is subtracted from it to create a fraction between 0 and 1. This fractional number is then multiplied by 360 to produce the hue angle in the HSB color model.
     * @param saturation In the range 0.0..1.0
     * @param brightness In the range 0.0..1.0
     */
    override fun setHSB(hue: Float, saturation: Float, brightness: Float, frame: MutableFrame) {
        set(Color.getHSBColor(hue, saturation, brightness), frame)
    }

    /** @return A string representation of this bulb, indicating its channel indices. */
    override fun toString(): String {
        return "Bulb{R:$channelRed,G:$channelGreen,B:$channelBlue}"
    }

    /**
     * When this bulb is treated as a [BulbSet], this allows iteration
     * over "all" the bulbs in the set (which will be just this one).
     */
    override fun iterator(): Iterator<BulbRGB> {
        return listOf(this).iterator()
    }
}
