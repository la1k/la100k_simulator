package darkness.generator.scripts.uka19

import darkness.generator.api.effects.CW
import java.awt.Color

class CW19 : BaseScript() {
    override suspend fun run() {
        super.run()

        val warmWhite = Color(255, 197, 143)
        val skyBlue = Color(64, 156, 255)
        val framesPerDit = 3

        rgbFade(allBulbsGroup, skyBlue, 10)
        skip(30)

        // Using hexadecimal character codes so as to not easily reveal the UKEnavn
        val charCodes = listOf(0x76, 0x69, 0x76, 0x69, 0x6c, 0x6c, 0x65)
        val cwEffects = letters.zip(charCodes).map {
            (letter, charCode) -> CW(letter, "${charCode.toChar()}", warmWhite, framesPerDit)
        }

        for (cwEffect in cwEffects) {
            effect(cwEffect)
            skip(cwEffect.totalFrames + framesPerDit * 2)
            cwEffect.cancel()
        }

        skip(20)
        rgbFade(allBulbsGroup, Color.black, 10)
        skip(30)
    }
}
