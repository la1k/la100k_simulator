package darkness.generator.scripts.uka19

import darkness.generator.api.BulbGroup

class FlashLetters: BaseScript() {
    override suspend fun run() {
        super.run()
        var previousLetter : BulbGroup? = null

        for (letter in letters) {
            if (previousLetter != null) {
                set(previousLetter, 0, 0, 0)
            }
            previousLetter = letter
            set(letter, 128, 128, 128)
            skip(10)
        }
    }
}
