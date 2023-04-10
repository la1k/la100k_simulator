package darkness.generator.api.effects

import darkness.generator.api.BulbRGB
import kotlin.math.pow
import kotlin.math.sqrt

/**
 * A rainbow effect that sources out from a single point with a given radius.
 * The center point can be moved during the effect giving cool results
 */
class PointRainbow(
    bulbs: List<BulbRGB>,
    centerPos: FloatArray,
    private val radius: Float
) : EffectBase() {
    private val bulbs = bulbs.toList()
    private val centerPos = floatArrayOf(centerPos[0], centerPos[1], centerPos.elementAtOrElse(2) { 0f })
    var saturation = 1.0f
    var brightness = 1.0f

    override suspend fun run() {
        while (!isCancelled) {
            for (bulb in bulbs) {
                // Calculate the distance between the bulb and the center
                val pos = bulb.position
                val distance = sqrt(
                    (centerPos[0] - pos[0]).pow(2) +
                    (centerPos[1] - pos[1]).pow(2) +
                    (centerPos[2] - pos[2]).pow(2))

                val colorAngle = distance / radius % 1.0f
                setHSB(bulb, colorAngle, saturation, brightness)
            }
            next()
        }

    }

    fun setCenterPos(x: Float, y: Float, z: Float = 0.0f) {
        centerPos[0] = x
        centerPos[1] = y
        centerPos[2] = z
    }

    override fun toString() = "PointRainbow running on (some bulb data here)"
}
