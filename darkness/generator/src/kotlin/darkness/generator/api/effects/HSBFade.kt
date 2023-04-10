package darkness.generator.api.effects

import darkness.generator.api.BulbSet

import java.awt.Color

/**
 * @todo Implement HSBColor instead of float array
 */
class HSBFade(
    private val bulbSet: BulbSet,
    private val endHSB: FloatArray,
    private val frames: Int
) : EffectBase() {
    private val startHSB: FloatArray = Color.RGBtoHSB(bulbSet.red, bulbSet.green, bulbSet.blue, null)

    override suspend fun run() {
        var frame = 1
        while (frame <= frames && !isCancelled) {
            val h = startHSB[0] + (endHSB[0] - startHSB[0]) * frame / frames
            val s = startHSB[1] + (endHSB[1] - startHSB[1]) * frame / frames
            val b = startHSB[2] + (endHSB[2] - startHSB[2]) * frame / frames

            setHSB(bulbSet, h, s, b)
            next()
            ++frame
        }
    }

    override fun toString() =
        "Effect HSBFade on $bulbSet from color: [h=${startHSB[0]},s=${startHSB[1]},b=${startHSB[2]}] " +
        "to color: [h=${endHSB[0]},s=${endHSB[1]},b=${endHSB[2]}] over $frames frames."
}
