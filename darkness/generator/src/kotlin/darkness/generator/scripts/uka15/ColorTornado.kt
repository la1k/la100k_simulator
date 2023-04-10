package darkness.generator.scripts.uka15

class ColorTornado : BaseScript() {
    override suspend fun run() {
        super.run()

        skip(20)
        for (letter in letters) {
            rgbFade(letter, 0, 0, 0, 20)
        }
        skip(30)

        for (t in 0..4) {
            for (c in 0..4) {
                hsbFade(letters[0], floatArrayOf(c.toFloat() / 5, 1f, 1.0f), 1)
                hsbFade(letters[6], floatArrayOf(c.toFloat() / 5, 1f, 0f), 1)
                skip(10 - t * 2 + 1)
                for (l in 1 until letters.size) {
                    hsbFade(letters[l], floatArrayOf(c.toFloat() / 5, 1f, 1.0f), 1)
                    hsbFade(letters[l - 1], floatArrayOf(c.toFloat() / 5, 1f, 0f), 1)
                    skip(10 - t * 2 + 1)
                }
            }
        }

        for (letter in letters) {
            rgbFade(letter, 0, 0, 0, 20)
        }
        skip(20)
    }
}
