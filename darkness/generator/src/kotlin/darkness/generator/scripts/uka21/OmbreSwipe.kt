package darkness.generator.scripts.uka21

import java.awt.Color
// ombreswipe fra venstre til høyre

class OmbreSwipe: BaseScript() {
    override suspend fun run() {
        super.run()

        val blå = Color(7, 0, 204)
        val rød = Color (179, 0, 41)
        val gul = Color(204, 190, 0)
        val oransj = Color(204, 105, 0)

        val startFargeListe = listOf(blå, rød, gul, oransj)

        for (startColor in startFargeListe) {
            val hsvValues = Color.RGBtoHSB(startColor.red, startColor.green, startColor.blue, null)
            val hue = hsvValues[0]
            val brightness = hsvValues[2]
            val fargeListe = mutableListOf(startColor)
            for (i in 1..letters.size) {
                val saturation = 1 - i/10.0f
                fargeListe.add(Color.getHSBColor(hue, saturation, brightness))
            }
            for (startPoint in 0 until letters.size) {
                var colorIndex = startPoint
                for (letter in letters.take(startPoint+1)) {
                    set(letter, fargeListe[colorIndex])
                    colorIndex = (colorIndex - 1 + letters.size) % letters.size
                }
                skip(10)
            }
        }
    }
}