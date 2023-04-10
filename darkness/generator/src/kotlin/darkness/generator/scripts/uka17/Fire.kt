package darkness.generator.scripts.uka17

import darkness.generator.api.effects.Aurora
import java.awt.Color

class Fire : BaseScript() {
    override suspend fun run() {
        super.run()

        val red = group(
                0, 7,
                15, 16,

                37, 38, 39,
                45, 50,

                67, 68, 69,
                78,
                95
        )

        val orange = group(
                1,
                17, 18, 19,

                33, 36,
                51, 49,

                63, 66,
                77, 79, 82,
                85, 86, 90
        )

        val yellow = group(
                2, 3, 8, 10,
                20, 21,
                25,
                32, 35, 40, 42,
                46, 52, 53,
                55,
                62, 65, 70, 60,
                76, 83, 80,
                87, 91, 93
        )

        val time = 20
        val fade = 4

        effect(Aurora(red, Color.RED, time, fade, red.numBulbs, 0.3f))
        effect(Aurora(orange, Color.ORANGE, time, fade, orange.numBulbs, 0.3f))
        effect(Aurora(yellow, Color.YELLOW, time, fade, yellow.numBulbs, 0.3f))
        skip(time * 20)
        fadeOut(20)
    }

    private suspend fun fadeOut(frames: Int) {
        for (bulb in allBulbsRGB) {
            rgbFade(bulb, Color.BLACK, frames)
        }
    }
}
