package darkness.generator.api.effects

import darkness.generator.api.BulbSet

class WeightedStrobe(
    private val bulbSet: BulbSet,
    private val tOn: Int,
    private val tOff: Int,
    private val repeat: Int
) : EffectBase() {
    override suspend fun run() {
        for (i in 0 until repeat) {
            val color = bulbSet.color
            set(bulbSet, 0, 0, 0)
            skip(tOff)
            set(bulbSet, color)
            skip(tOn)
        }
    }

    override fun toString() = "Effect WeightedStrobe on $bulbSet with t on: $tOn t off: $tOff for $repeat times."
}
