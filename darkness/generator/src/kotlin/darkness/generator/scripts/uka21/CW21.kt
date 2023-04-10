package darkness.generator.scripts.uka21

import darkness.generator.api.effects.CW
import java.awt.Color

class CW21 : BaseScript() {
    override suspend fun run() {
        super.run()

        val warmWhite = Color(255, 197, 143)
        val skyBlue = Color(64, 156, 255)
        val framesPerDit = 2

        rgbFade(allBulbsGroup, skyBlue, 10)
        skip(15)

        // Using hexadecimal character codes so as to not easily reveal the UKEnavn
        val charCodes = listOf(0x72, 0x61, 0x7a, 0x7a, 0x6d, 0x61, 0x74, 0x61, 0x7a, 0x7a)
        val cwEffects = letters.zip(charCodes).map {
            (letter, charCode) -> CW(letter, "${charCode.toChar()}", warmWhite, framesPerDit)
        }

        for (cwEffect in cwEffects) {
            effect(cwEffect)
            skip(cwEffect.totalFrames + framesPerDit * 1)
            cwEffect.cancel()
        }

        skip(10)
        rgbFade(allBulbsGroup, Color.black, 10)
        skip(15)
    }
}
