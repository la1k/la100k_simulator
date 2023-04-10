package darkness.generator.scripts.uka17

class RGBTest : BaseScript() {
    override suspend fun run() {
        super.run()
        for (letter in letters) {
            set(letter, 255, 0, 0)
        }
        skip(20)
        for (letter in letters) {
            set(letter, 0, 255, 0)
        }
        skip(20)
        for (letter in letters) {
            set(letter, 0, 0, 255)
        }
        skip(20)
    }
}
