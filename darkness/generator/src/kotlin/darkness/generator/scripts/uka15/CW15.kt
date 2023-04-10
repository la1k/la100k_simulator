package darkness.generator.scripts.uka15

import darkness.generator.api.effects.CW

import java.awt.*

class CW15 : BaseScript() {
    override suspend fun run() {
        super.run()

        val warmWhite = Color(255, 197, 143)
        val skyBlue = Color(64, 156, 255)

        // Defs

        rgbFade(A, warmWhite, 10)
        rgbFade(B, warmWhite, 10)
        rgbFade(C, warmWhite, 10)
        rgbFade(D, warmWhite, 10)
        rgbFade(E, warmWhite, 10)
        rgbFade(F, warmWhite, 10)
        rgbFade(G, warmWhite, 10)
        skip(10)

        skip(20)

        val framesPerDit = 3


        val cwA = CW(A, "l", skyBlue, framesPerDit)
        val cwB = CW(B, "u", skyBlue, framesPerDit)
        val cwC = CW(C, "r", skyBlue, framesPerDit)
        val cwD = CW(D, "i", skyBlue, framesPerDit)
        val cwE = CW(E, "f", skyBlue, framesPerDit)
        val cwF = CW(F, "a", skyBlue, framesPerDit)
        val cwG = CW(G, "x", skyBlue, framesPerDit)

        effect(cwA)
        skip(cwA.totalFrames)
        skip(framesPerDit * 2)
        cwA.cancel()

        effect(cwB)
        skip(cwB.totalFrames)
        skip(framesPerDit * 2)
        cwB.cancel()

        effect(cwC)
        skip(cwC.totalFrames)
        skip(framesPerDit * 2)
        cwC.cancel()

        effect(cwD)
        skip(cwD.totalFrames)
        skip(framesPerDit * 2)
        cwD.cancel()

        effect(cwE)
        skip(cwE.totalFrames)
        skip(framesPerDit * 2)
        cwE.cancel()

        effect(cwF)
        skip(cwF.totalFrames)
        skip(framesPerDit * 2)
        cwF.cancel()

        effect(cwG)
        skip(cwG.totalFrames)
        skip(framesPerDit * 2)
        cwG.cancel()

        skip(20)


        rgbFade(A, Color.black, 10)
        rgbFade(B, Color.black, 10)
        rgbFade(C, Color.black, 10)
        rgbFade(D, Color.black, 10)
        rgbFade(E, Color.black, 10)
        rgbFade(F, Color.black, 10)
        rgbFade(G, Color.black, 10)
        skip(10)

        skip(20)

        /*
        //BulbGroup cCentre = group(37, 38, 39, 40);

        CW cw = new CW(cCentre, "lurifax", warmWhite, 3);
        effect(cw);

        skip(cw.getTotalFrames());

        skip(10);
        */
    }
}
