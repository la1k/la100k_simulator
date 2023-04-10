package darkness.generator.scripts.uka17

import darkness.generator.api.effects.WeightedStrobe
import java.awt.Color

class Jubileum : BaseScript() {
    override suspend fun run() {
        super.run()

        val c = Color(255, 255, 255)
        val t = 3
        val fade_ext = 17

        for (digit in digits) {
            for (bulb in digit) {
                rgbFade(bulb, c, t + fade_ext)
                skip(t)
            }
            skip(fade_ext)
        }
        // hardcoded fill last element
        rgbFade(bulb(95), c, t + fade_ext)
        skip(t)
        for (i in 0..4) {
            rgbFade(bulb(85 + i), c, t + fade_ext)
            rgbFade(bulb(90 + i), c, t + fade_ext)
            skip(t)
        }
        skip(fade_ext)
        // flash last \0/
        effect(WeightedStrobe(I, 3 * t, t, 3))
        skip(3 * t)
    }
}
