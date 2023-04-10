package darkness.generator.scripts.uka15

import darkness.generator.api.BulbRGB

import java.awt.*

class LGBT : BaseScript() {
    override suspend fun run() {
        super.run()
        val fallFrames = 30
        for (i in 0 until fallFrames) {
            for (bulb in allBulbs) {
                val y = bulb.position[1]
                if (y >= LETTER_HEIGHT * i / fallFrames && y < LETTER_HEIGHT * (i + 1) / fallFrames) {
                    rgbFade(bulb, Color.getHSBColor(getHue(bulb), 1f, 1f), 30)
                }
            }
            next()
        }
        skip(40)

        for (i in 0..1) {
            for (bulb in allBulbs) {
                val hsb = FloatArray(3)
                Color.RGBtoHSB(bulb.red, bulb.green, bulb.blue, hsb)
                hsbFade(bulb, hsb[0] + 1, 1f, 1f, 40)
            }
            skip(40)
        }

        for (i in 0..3) {
            for (bulb in allBulbs) {
                rgbFade(bulb, Color.getHSBColor(getHue(bulb), 1f, 0.5f), 10)
            }
            skip(10)
            for (bulb in allBulbs) {
                rgbFade(bulb, Color.getHSBColor(getHue(bulb), 1f, 1f), 10)
            }
            skip(10)
        }
        skip(40)
        for (bulb in allBulbs) {
            rgbFade(bulb, 0, 0, 0, 20)
        }
        skip(20)
    }

    private fun getHue(bulb: BulbRGB): Float {
        val y = bulb.position[1]
        return ((1 - y / LETTER_HEIGHT) * 0.8).toFloat()
    }
}
