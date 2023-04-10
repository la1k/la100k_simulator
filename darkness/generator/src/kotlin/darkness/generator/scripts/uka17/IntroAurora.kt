package darkness.generator.scripts.uka17

import darkness.generator.api.effects.Aurora
import darkness.generator.api.effects.WeightedStrobe
import java.awt.Color

class IntroAurora : BaseScript() {
    override suspend fun run() {
        super.run()

        val green = Color(57, 255, 20) // neon green
        val warmWhite = Color(218, 165, 32)

        val longWaitTime = 1 // TODO: increase to number of seconds

        effect(Aurora(mergedAllBulbs, warmWhite, 1, 4, 30, 0.0f))
        skip(1 * 20)

        effect(Aurora(mergedAllBulbs, warmWhite, 20, 4, 30, 0.0f))

        skip(5 * 20)
        effect(Aurora(A, green, 15, 4, 30, 0.5f))
        effect(Aurora(B, green, 15, 4, 30, 0.5f))

        skip(5 * 20)
        effect(Aurora(D, green, 10, 4, 30, 0.5f))
        effect(Aurora(E, green, 10, 4, 30, 0.5f))

        skip(5 * 20)
        effect(Aurora(G, green, 5, 4, 30, 0.5f))
        effect(Aurora(H, green, 5, 4, 30, 0.5f))
        effect(Aurora(I, green, 5, 4, 30, 0.5f))
        skip(5 * 20 + 1)

        set(C, 0, 0, 0)
        set(F, 0, 0, 0)
        set(A, green)
        set(B, green)
        set(D, green)
        set(E, green)
        set(G, green)
        set(H, green)
        set(I, green)
        skip(1 * 20)

        effect(WeightedStrobe(A, 5, 2, 3))
        effect(WeightedStrobe(B, 5, 2, 3))
        effect(WeightedStrobe(D, 5, 2, 3))
        effect(WeightedStrobe(E, 5, 2, 3))
        effect(WeightedStrobe(G, 5, 2, 3))
        effect(WeightedStrobe(H, 5, 2, 3))
        effect(WeightedStrobe(I, 5, 2, 3))
        skip(1 * 20)
        rgbFade(A, Color.BLACK, 10)
        rgbFade(B, Color.BLACK, 10)
        rgbFade(D, Color.BLACK, 10)
        rgbFade(E, Color.BLACK, 10)
        rgbFade(G, Color.BLACK, 10)
        rgbFade(H, Color.BLACK, 10)
        rgbFade(I, Color.BLACK, 10)

        val t = 3
        val fade_ext = 17

        for (digit in digits) {
            for (bulb in digit) {
                rgbFade(bulb, warmWhite, t + fade_ext)
                skip(t)
            }
            skip(fade_ext)
        }
        // hardcoded fill last element
        rgbFade(bulb(95), warmWhite, t + fade_ext)
        skip(t)
        for (i in 0..4) {
            rgbFade(bulb(85 + i), warmWhite, t + fade_ext)
            rgbFade(bulb(90 + i), warmWhite, t + fade_ext)
            skip(t)
        }
        skip(fade_ext)
        // flash last \0/
        effect(WeightedStrobe(I, 3 * t, t, 3))
        skip(3 * t)
    }
}
