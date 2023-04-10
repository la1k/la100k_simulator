package darkness.generator.scripts.uka21

import java.awt.Color

class Znork : BaseScript() {
    override suspend fun run() {
        super.run()
        
        val color = Color(255, 127, 0)
        val fadeDuration = 25
        val spacing = 10

        for (i in 0..2) {
            for (bulb in A.allBulbs.drop(4)) {
                rgbFade(bulb, color, fadeDuration)
            }
            skip(spacing)
            rgbFade(C, color, fadeDuration)
            skip(spacing)
            rgbFade(D, color, fadeDuration)
            skip(spacing)
            rgbFade(I, color, fadeDuration)
            skip(spacing)
            rgbFade(J, color, fadeDuration)
            skip(fadeDuration * 2)
            for (bulb in allBulbs) {
                rgbFade(bulb, 0, 0, 0, fadeDuration)
            }
            skip(fadeDuration)
        }
    }
}
