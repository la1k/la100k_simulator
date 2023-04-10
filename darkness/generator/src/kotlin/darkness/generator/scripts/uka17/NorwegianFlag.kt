package darkness.generator.scripts.uka17

import darkness.generator.api.effects.Aurora
import java.awt.Color

class NorwegianFlag : BaseScript() {
    override suspend fun run() {
        super.run()

        val groups = listOf(group(
            0, 4, 5, 6, 10, 11, 15, 16, 17, 19,
            45, 49, 50, 60, 64, 68, 72, 73, 77, 78,
            9, 82, 88, 89, 93, 94, 95
        ), group(
            1, 3, 7, 9, 18, 20, 25, 30, 31, 33,
            39, 46, 51, 55, 63, 67, 69, 76, 83, 80,
            85, 90, 87, 92, 42, 41, 48, 53, 54, 61,
            65, 71
        ), group(
            2, 8, 21, 26, 32, 36, 40, 47, 52, 56,
            62, 66, 70, 75, 84, 81, 86, 91, 43, 34,
            35, 37, 38
        ))

        val colors = listOf(Color(0xED, 0x29, 0x39), Color(0xFF, 0XFF, 0XFF), Color(0x00, 0x26 + 0x15, 0x64 + 0x40))

        val time = 20
        val fade = 4

        for (i in groups.indices) {
            effect(Aurora(groups[i], colors[i], time, fade, groups[i].numBulbs, 0.4f))
        }
        skip(time * 20)
        fadeOut(20)
    }

    private suspend fun fadeOut(frames: Int) {
        for (bulb in allBulbsRGB) {
            rgbFade(bulb, Color.BLACK, frames)
        }
    }
}
