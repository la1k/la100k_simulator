package darkness.generator.scripts.la100k

import darkness.generator.api.effects.CW
import java.awt.Color

class CWla100k:BaseScript() {
    override suspend fun run() {
        super.run()

        val white = Color(255, 255, 255)
        val name = "la100k"
        val framesPerDit = 2

        letters.zip(name.toList()).forEach { (letter, character) ->
            val cwEffect = CW(letter, "$character", white, framesPerDit)
            effect(cwEffect)
            skip(cwEffect.totalFrames + framesPerDit)
            //cwEffect.cancel()
        }
        skip(10)
    }
}
