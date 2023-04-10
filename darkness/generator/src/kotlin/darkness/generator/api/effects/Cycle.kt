package darkness.generator.api.effects

import darkness.generator.api.BulbSet

import java.awt.Color

class Cycle(
    private val onColor: Color,
    private val offColor: Color?,
    private val period: Int,
    private val bulbSets: List<BulbSet>
) : EffectBase() {
    override suspend fun run() {
        var currentSet = 0

        while (!isCancelled) {
            for (f in 0 until period) {
                for (i in 0 until bulbSets.size) {
                    if (i == currentSet) {
                        set(bulbSets[i], onColor)
                    } else {
                        if (offColor == null) {
                            relinquish(bulbSets[i])
                        } else {
                            set(bulbSets[i], offColor)
                        }
                    }
                }

                next()
            }

            ++currentSet

            if (currentSet == bulbSets.size)
                currentSet = 0
        }
    }

    override fun toString() = "Effect Cycle on $bulbSets for color $onColor (off color $offColor) with period $period."
}
