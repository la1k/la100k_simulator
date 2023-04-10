package darkness.generator.scripts.uka17

import darkness.generator.api.BulbRGB
import java.awt.Color
import java.util.*

class RandomFade : BaseScript() {
    private val random = Random(1337)

    private val randomColor: Color
        get() = Color(random.nextInt(256), random.nextInt(256), random.nextInt(256))

    private val shuffledBulbs: List<BulbRGB>
        get() = allBulbsRGB.shuffled().toList()

    override suspend fun run() {
        super.run()

        var loops = 10
        while (loops > 0) {
            fade(10, randomColor)
            loops--
        }
        fadeOut(10)
    }

    private suspend fun fade(duration: Int, color: Color) {
        for (bulb in shuffledBulbs) {
            rgbFade(bulb, color, duration)
            skip(1)
        }
    }

    private suspend fun fadeOut(duration: Int) {
        fade(duration, Color.BLACK)
    }
}
