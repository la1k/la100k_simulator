package darkness.generator.scripts.uka21

import java.awt.Color

class LetterTest : BaseScript() {
    override suspend fun run() {
        super.run()
        
        for(letter in letters){
            set(letter, 255, 0, 0)
            skip(2)
            set(letter, 0, 255, 0)
            skip(2)
            set(letter, 0, 0, 255)
            skip(2)
            set(letter, 0, 0, 0)
            skip(2)
        }

    }
}