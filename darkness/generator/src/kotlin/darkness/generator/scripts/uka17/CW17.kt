package darkness.generator.scripts.uka17

import darkness.generator.api.effects.CW
import java.awt.Color

class CW17 : BaseScript() {

    override suspend fun run() {
        super.run()

        // Change up colours
        val warmerWhite = Color(255, 214, 170)
        val warmWhite = Color(255, 197, 143)
        val skyBlue = Color(64, 156, 255)

        // Defs

        rgbFade(A, warmWhite, 10)
        rgbFade(B, warmWhite, 10)
        //rgbFade(C, restColor, 10);
        rgbFade(D, warmWhite, 10)
        rgbFade(E, warmWhite, 10)
        //rgbFade(F, restColor, 10);
        rgbFade(G, warmWhite, 10)
        rgbFade(H, warmWhite, 10)
        rgbFade(I, warmWhite, 10)
        skip(10)

        skip(20)

        val framesPerDit = 3

        val cwA = CW(A, "t", skyBlue, framesPerDit)
        val cwB = CW(B, "a", skyBlue, framesPerDit)
        //CW cwC = new CW(C, "c", morseColor, framesPerDit);
        val cwD = CW(D, "d", skyBlue, framesPerDit)
        val cwE = CW(E, "e", skyBlue, framesPerDit)
        //CW cwF = new CW(F, "f", morseColor, framesPerDit);
        val cwG = CW(G, "d", skyBlue, framesPerDit)
        val cwH = CW(H, "u", skyBlue, framesPerDit)
        val cwI = CW(I, "!", skyBlue, framesPerDit)

        effect(cwA)
        skip(cwA.totalFrames)
        skip(framesPerDit * 2)
        cwA.cancel()

        effect(cwB)
        skip(cwB.totalFrames)
        skip(framesPerDit * 2)
        cwB.cancel()

        /*
        effect(cwC);
        skip(cwC.getTotalFrames());
        skip(framesPerDit * 2);
        cwC.cancel();
        */

        effect(cwD)
        skip(cwD.totalFrames)
        skip(framesPerDit * 2)
        cwD.cancel()

        effect(cwE)
        skip(cwE.totalFrames)
        skip(framesPerDit * 2)
        cwE.cancel()

        /*
        effect(cwF);
        skip(cwF.getTotalFrames());
        skip(framesPerDit * 2);
        cwF.cancel();
        */

        effect(cwG)
        skip(cwG.totalFrames)
        skip(framesPerDit * 2)
        cwG.cancel()

        effect(cwH)
        skip(cwH.totalFrames)
        skip(framesPerDit * 2)
        cwH.cancel()

        effect(cwI)
        skip(cwI.totalFrames)
        skip(framesPerDit * 2)
        cwI.cancel()

        skip(20)

        rgbFade(A, Color.black, 10)
        rgbFade(B, Color.black, 10)
        rgbFade(C, Color.black, 10)
        rgbFade(D, Color.black, 10)
        rgbFade(E, Color.black, 10)
        rgbFade(F, Color.black, 10)
        rgbFade(G, Color.black, 10)
        rgbFade(H, Color.black, 10)
        rgbFade(I, Color.black, 10)
        skip(10)

        skip(20)
    }
}
