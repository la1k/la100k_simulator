package darkness.generator.scripts.uka15

import java.awt.Color

class NorwegianFlag : BaseScript() {
    override suspend fun run() {
        super.run()
        var y = LETTER_HEIGHT
        while (y >= -0.05) {
            for (i in 0..8) {
                for (bulb in columns[i]) {
                    if (bulb.position[1] >= y) {
                        set(bulb, 0xED, 0x29, 0x39)
                    }
                }
            }
            for (bulb in columns[8]) {
                if (bulb.position[1] >= y) {
                    set(bulb, Color.WHITE)
                }
            }
            for (i in 9..10) {
                for (bulb in columns[i]) {
                    if (bulb.position[1] >= y) {
                        set(bulb, 0x00, 0x26 + 0x15, 0x64 + 0x40) // Needs to be lighter than the actual color (#002664) because the blue light is harder to see
                    }
                }
            }
            for (bulb in columns[11]) {
                if (bulb.position[1] >= y) {
                    set(bulb, Color.WHITE)
                }
            }
            for (i in 12 until columns.size) {
                for (bulb in columns[i]) {
                    if (bulb.position[1] >= y) {
                        set(bulb, 0xED, 0x29, 0x39)
                    }
                }
            }
            next()
            y -= 0.05
        }
        skip(80)
        for (bulb in allBulbs) {
            rgbFade(bulb, Color.BLACK, 40)
        }
        skip(20)
    }
}
