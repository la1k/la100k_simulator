package darkness.generator.scripts.uka21

import darkness.generator.api.BulbGroup

class ColorTest : BaseScript() {
    override suspend fun run() {
        super.run()

        for (letter in letters) {
            set(letter, 255, 255, 0)
        }
        skip(50)
        for (letter in letters) {
            set(letter, 0, 255, 255)
        }
        skip(50)
        for (letter in letters) {
            set(letter, 255, 0, 255)
        }
        skip(50)
    }
}
