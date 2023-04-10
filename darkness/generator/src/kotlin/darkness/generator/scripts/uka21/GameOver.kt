package darkness.generator.scripts.uka21

import java.awt.Color

class GameOver : BaseScript() {
    override suspend fun run() {
        super.run()

        val maxHeight = allBulbs.maxOf { it.position[1] }
        val minHeight = allBulbs.minOf { it.position[1] }
        println("Height: $minHeight $maxHeight")

        var y = maxHeight
        val step = (maxHeight - minHeight) / (60 * 20)
        val band = 0.1f
        val color = uke_gull

        while (y >= minHeight - band) {
            for (bulb in allBulbs) {
                val bulbY = bulb.position[1]
                if (bulbY <= y) {
                    set(bulb, color)
                } else if (bulbY <= y + band) {
                    val c = 1f - 1f / band * (bulbY - y)
                    set(bulb, (color.red * c).toInt(), (color.green * c).toInt(), (color.blue * c).toInt())
                } else {
                    set(bulb, 0, 0, 0)
                }
            }
            skip(1)
            y -= step
        }

        set(allBulbsGroup, 0, 0, 0)

        val uke_rød_dark = Color(115, 0, 0)
        skip(20)
        rgbFade(E, uke_rød, 20)
        skip(20)
        rgbFade(E, uke_rød_dark, 10)
        skip(20)
        rgbFade(E, uke_rød, 10)
        skip(10)
        rgbFade(E, uke_rød_dark, 10)
        skip(20)
        rgbFade(E, uke_rød, 10)
        skip(10)
        rgbFade(E, 0, 0, 0, 20)
        skip(20)

        // Remain dark while we unplug the power
        skip(60 * 20)
    }
}
