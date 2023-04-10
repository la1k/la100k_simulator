package darkness.generator.scripts.uka17

import darkness.generator.api.effects.WeightedStrobe
import java.awt.Color

class Pride : BaseScript() {
    override suspend fun run() {
        super.run()

        val bg = Color(255, 255, 255)
        val t_base = 3
        val t_on = 3
        val t_off = 0
        val n_flash = 5

        for (bulb in allBulbsRGB) {
            rgbFade(bulb, bg, t_base * (t_on + t_off) * n_flash)
        }
        skip(t_base * (t_on + t_off) * n_flash)

        effect(WeightedStrobe(A, t_base * t_on, t_base * t_off, n_flash))
        effect(WeightedStrobe(B, t_base * t_on, t_base * t_off, n_flash))
        rgbFade(A, Color.RED, t_base * (t_on + t_off) * n_flash)
        rgbFade(B, Color.RED, t_base * (t_on + t_off) * n_flash)
        skip(t_base * (t_on + t_off) * n_flash)

        rgbFade(C, Color.ORANGE, t_base * (t_on + t_off) * n_flash)
        skip(t_base * (t_on + t_off))

        effect(WeightedStrobe(D, t_base * t_on, t_base * t_off, n_flash))
        effect(WeightedStrobe(E, t_base * t_on, t_base * t_off, n_flash))
        rgbFade(D, Color.YELLOW, t_base * (t_on + t_off) * n_flash)
        rgbFade(E, Color.YELLOW, t_base * (t_on + t_off) * n_flash)
        skip(t_base * (t_on + t_off) * n_flash)

        rgbFade(F, Color.GREEN, t_base * (t_on + t_off) * n_flash)
        skip(t_base * (t_on + t_off))

        effect(WeightedStrobe(G, t_base * t_on, t_base * t_off, n_flash))
        effect(WeightedStrobe(H, t_base * t_on, t_base * t_off, n_flash))
        effect(WeightedStrobe(I, t_base * t_on, t_base * t_off, n_flash))
        rgbFade(G, Color.BLUE, t_base * (t_on + t_off) * n_flash)
        rgbFade(H, Color.BLUE, t_base * (t_on + t_off) * n_flash)
        rgbFade(I, Color.MAGENTA, t_base * (t_on + t_off) * n_flash)
        skip(t_base * (t_on + t_off) * n_flash)

        for (bulb in allBulbsRGB) {
            rgbFade(bulb, Color.BLACK, t_base * (t_on + t_off) * n_flash)
        }
        skip(t_base * (t_on + t_off) * n_flash)
    }
}
