package darkness.generator.api

import java.awt.Color
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

/**
 * Either an individual bulb ([BulbRGB]), or a set of bulbs ([BulbGroup]).
 */
interface BulbSet : Iterable<BulbRGB> {
    /**
     * If this is a single bulb, return the red component of that bulb in the given frame.
     * Otherwise, return the red component of the first bulb in the group in the given frame.
     */
    fun redIn(frame: Frame): Int

    /**
     * If this is a single bulb, return the green component of that bulb in the given frame.
     * Otherwise, return the green component of the first bulb in the group in the given frame.
     */
    fun greenIn(frame: Frame): Int

    /**
     * If this is a single bulb, return the blue component of that bulb in the given frame.
     * Otherwise, return the blue component of the first bulb in the group in the given frame.
     */
    fun blueIn(frame: Frame): Int

    /**
     * If this is a single bulb, return the RGB color of that bulb in the given frame.
     * Otherwise, return the RGB color of the first bulb in the group in the given frame.
     */
    fun colorIn(frame: Frame): Color

    /**
     * Gets the position (or average of positions of multiple bulbs).
     * @return a 3 element array with x, y and z position. (Should be meters from the bottom left corner of the sign). z is normally set to 0.0f
     */
    val position: FloatArray

    /** Set the bulb(s) to the given RGB color in the given frame. */
    fun set(red: Int, green: Int, blue: Int, frame: MutableFrame)

    /** Set the bulb(s) to the given RGB color in the given frame. */
    fun set(color: Color, frame: MutableFrame)

    /** Set the bulb(s) to the given HSB color in the given frame. */
    fun setHSB(hue: Float, saturation: Float, brightness: Float, frame: MutableFrame)

    /**
     * Set the bulb(s) to the given RGB color in the given frame.
     * Out-of-range values will be coerced to [0-255].
     */
    fun setCoerced(red: Double, green: Double, blue: Double, frame: MutableFrame) {
        set(
            max(0, min(255, red.roundToInt())),
            max(0, min(255, green.roundToInt())),
            max(0, min(255, blue.roundToInt())),
            frame)
    }
}
