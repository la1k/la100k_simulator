package darkness.generator.scripts.uka21

import darkness.generator.api.BulbSet
import java.awt.Color

class Marit : BaseScript() {
    override suspend fun run() {
        super.run()

        val color = Color(255, 0, 0)
        val fadeDuration = 15
        val duration = 40
        val spacing = 10

        skip(spacing)
        for (bulbSet in listOf<BulbSet>(E, F, E, E, F)) {
            rgbFade(bulbSet, Color(255, 0, 0), fadeDuration)
            skip(fadeDuration + duration)
            rgbFade(bulbSet, Color(0, 0, 0), fadeDuration)
            skip(fadeDuration + spacing)
        }

        for (bulbSet in listOf<BulbSet>(E, F, A, group(72, 74, 75, 76), G)) {
            rgbFade(bulbSet, Color(255, 0, 0), fadeDuration)
            skip(fadeDuration + duration)
            rgbFade(bulbSet, Color(0, 0, 0), fadeDuration)
            skip(fadeDuration + spacing)
        }
    }
}
