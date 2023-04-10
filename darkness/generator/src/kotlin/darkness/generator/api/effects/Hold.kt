package darkness.generator.api.effects

import darkness.generator.api.BulbSet

import java.awt.*

class Hold(
    private val bulbSet: BulbSet,
    private val color: Color,
    private val frames: Int
) : EffectBase() {
    override suspend fun run() {
        var f = 0
        while (f < frames && !isCancelled) {
            set(bulbSet, color)
            next()
            f++
        }
    }

    override fun toString() = "Effect Hold on $bulbSet for color $color over $frames frames."
}
