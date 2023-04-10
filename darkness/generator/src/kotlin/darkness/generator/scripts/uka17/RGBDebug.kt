package darkness.generator.scripts.uka17

import java.awt.*

class RGBDebug : BaseScript() {
    override suspend fun run() {
        super.run()
        val colors = listOf(Color.RED, Color.GREEN, Color.BLUE)
        for (color in colors) {
            for (letter in letters) {
                rgbFade(letter, color, 80)
            }
            skip(80)
            for (letter in letters) {
                rgbFade(letter, Color.BLACK, 80)
            }
            skip(80)
        }
    }
}
