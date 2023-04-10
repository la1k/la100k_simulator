package darkness.generator.scripts.uka15

class SimenIntroStart : BaseScript() {
    override suspend fun run() {
        super.run()

        for (letter in letters) {
            set(letter, 0, 0, 0)
        }

        skip(20)
        for (letter in letters) {
            rgbFade(letter, 100, 100, 100, 20)
        }
        skip(20)
        for (letter in letters) {
            rgbFade(letter, 0, 0, 0, 1)
        }

        skip(20)
        for (letter in letters) {
            rgbFade(letter, 155, 155, 155, 40)
        }
        skip(40)
        for (letter in letters) {
            rgbFade(letter, 0, 0, 0, 1)
        }
        skip(20)

        for (letter in letters) {
            rgbFade(letter, 255, 255, 255, 40)
        }
        skip(60)
        for (letter in letters) {
            rgbFade(letter, 100, 100, 100, 20)
        }
        skip(20)
        for (letter in letters) {
            rgbFade(letter, 255, 255, 255, 20)
        }
        skip(20)
        skip(20)
        for (letter in letters) {
            rgbFade(letter, 200, 200, 200, 20)
        }
        skip(20)
        for (letter in letters) {
            rgbFade(letter, 255, 255, 255, 20)
        }

        skip(80)
    }
}
