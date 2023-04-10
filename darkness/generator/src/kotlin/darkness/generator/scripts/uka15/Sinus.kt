package darkness.generator.scripts.uka15

class Sinus : BaseScript() {
    override suspend fun run() {
        super.run()
        val middleBottom = 0.5
        val blurRadius = 0.2
        val frequencyMultiplier = 1.007
        val iterations = 400
        var frequency = 0.2
        var t = 0.0
        var i = 0
        while (i < iterations) {
            for (bulb in allBulbs) {
                val x = bulb.position[0]
                val y = bulb.position[1]
                val bottom = (1 - Math.abs(((LEFT + RIGHT) / 2 - x) / (WIDTH / 2))) * middleBottom
                val sin = bottom + (Math.sin((x - LEFT + t) * frequency) + 1) / 2 * (LETTER_HEIGHT - bottom)
                val red = (sin - (y - blurRadius)) / (blurRadius * 2) * 255
                if (i == 0) {
                    rgbFade(bulb, if (red < 0) 0 else if (red > 255) 255 else Math.round(red).toInt(), 0, 0, 20)
                } else {
                    setCoerced(bulb, red, 0.0, 0.0)
                }
            }
            if (i == 0) {
                skip(20)
            } else {
                next()
            }
            t += 0.03
            i++
            frequency *= frequencyMultiplier
        }
        for (bulb in allBulbs) {
            rgbFade(bulb, 0, 0, 0, 20)
        }
    }
}
