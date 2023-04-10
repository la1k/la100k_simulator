package darkness.generator.scripts.uka15

import darkness.generator.api.BulbRGB
import darkness.generator.api.effects.EffectBase

class SnowEffect(private val allBulbs: List<BulbRGB>, private val xTarget: Double) : EffectBase() {
    private val LETTER_HEIGHT = 1.5
    private val LEFT = 1.73
    private val RIGHT = 8.62
    private val WIDTH = RIGHT - LEFT

    override suspend fun run() {
        val radius = LETTER_HEIGHT / 5
        val extraDepth = radius * 1.5
        val duration = 40
        for (i in 0 until duration) {
            val yTarget = (1 - Math.pow(1 - (duration - i.toDouble()) / duration * 0.75, 2.0)) * (LETTER_HEIGHT + extraDepth) - extraDepth
            for (bulb in allBulbs) {
                val x = bulb.position[0].toDouble()
                val y = bulb.position[1].toDouble()
                val c = 255 * (1 - Math.sqrt(Math.pow(xTarget - x, 2.0) + Math.pow(yTarget - y, 2.0)) / radius)
                if (c <= 10) {
                    relinquish(bulb)
                } else {
                    setCoerced(bulb, c, c, c)
                }
            }
            next()
        }
        skip(40)
    }

    override fun toString() = "SnowEffect"
}
