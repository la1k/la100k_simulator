package darkness.generator.api.effects

import darkness.generator.api.BulbRGB
import darkness.generator.api.BulbSet

import java.awt.Color

class RGBFade(
    private val bulbSet: BulbSet,
    private val startColor: Color,
    private val endColor: Color,
    private val frames: Int
) : EffectBase() {
    override suspend fun run() {
        val delta_r = (endColor.red - startColor.red).toFloat() / frames.toFloat()
        val delta_g = (endColor.green - startColor.green).toFloat() / frames.toFloat()
        val delta_b = (endColor.blue - startColor.blue).toFloat() / frames.toFloat()

        var frame = 1
        while (frame <= frames && !isCancelled) {
            val red = startColor.red + (delta_r * frame).toInt()
            val green = startColor.green + (delta_g * frame).toInt()
            val blue = startColor.blue + (delta_b * frame).toInt()

            set(bulbSet, red, green, blue)
            next()
            frame++
        }
    }

    override fun toString() = "Effect RGBFade on $bulbSet from color: $startColor to color: $endColor over $frames frames."
}
