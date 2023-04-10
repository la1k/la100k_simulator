package darkness.generator.scripts.uka15

import darkness.generator.api.effects.EffectBase
import darkness.generator.api.effects.Hold
import darkness.generator.api.effects.RGBFade
import java.awt.Color
import java.util.*

class Snow : BaseScript() {
    override suspend fun run() {
        super.run()
        val random = Random(42)
        val xes = columns
            .map { c -> c.getBulb(0).position[0] }
            .filter { it > 1.3 }
        var snowLevel = -0.5
        val snowHeight = LETTER_HEIGHT - snowLevel
        val iterations = 100
        val sleep = 3
        val effects = mutableListOf<EffectBase>()
        val fixed = HashSet<Int>()
        for (i in 0 until iterations) {
            val x = xes[random.nextInt(xes.size)]
            effect(SnowEffect(allBulbs, x.toDouble()))
            snowLevel += snowHeight / iterations
            for (bulb in allBulbs) {
                if (bulb.position[1] < snowLevel && !fixed.contains(bulb.id)) {
                    val hold = Hold(bulb, Color.WHITE, sleep * (iterations - i + 60))
                    hold.priority = 1
                    effect(hold)
                    effects.add(hold)
                    fixed.add(bulb.id)
                }
            }
            skip(sleep)
        }
        skip(60)
        for (bulb in allBulbs) {
            val fade = RGBFade(bulb, bulb.color, Color.BLACK, 40)
            fade.priority = 2
            effect(fade)
            effects.add(fade)
        }
        skip(40)
        for (effect in effects) {
            effect.cancel()
        }
    }
}
