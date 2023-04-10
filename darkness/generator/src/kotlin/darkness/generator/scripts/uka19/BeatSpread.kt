package darkness.generator.scripts.uka19

import darkness.generator.api.BulbGroup
import darkness.generator.api.effects.Aurora
import java.awt.Color

class BeatSpread : BaseScript(){
    override suspend fun run() {
        super.run()

        val offWhite: Color = Color(80,80,80)
        val bloodRed: Color = Color(102, 10, 10)
        val beatRed: Color = Color(165, 15, 15)                 //Haha pun.
        val red: Color = Color(150, 10, 10)

        //Beat 1
        rgbFade(C, bloodRed, 15)
        rgbFade(allBulbsGroup, offWhite, 10)
        skip(15)

        rgbFade(C, beatRed, 10)
        skip(10)
        set(C, bloodRed)

        set(B, beatRed)
        set(D, beatRed)
        rgbFade(B, Color(0,0,0), 10)
        rgbFade(D, Color(0,0,0), 10)
        skip(5)

        set(A, beatRed)
        set(E, beatRed)
        rgbFade(A, Color(0,0,0), 10)
        rgbFade(E, Color(0,0,0), 10)
        skip(5)

        set(F, beatRed)
        rgbFade(F, Color(0,0,0), 10)
        skip(5)

        set(G, beatRed)
        rgbFade(G, red, 10)
        skip(5)

        //Beat 2
        rgbFade(C, bloodRed, 10)
        rgbFade(G, red, 10)
        rgbFade(allBulbsGroup, offWhite, 10)
        skip(10)

        rgbFade(C, beatRed, 10)
        skip(10)
        set(C, bloodRed)

        set(B, beatRed)
        set(D, beatRed)
        rgbFade(B, Color(0,0,0), 10)
        rgbFade(D, Color(0,0,0), 10)
        skip(5)

        set(A, beatRed)
        set(E, beatRed)
        rgbFade(A, Color(0,0,0), 10)
        rgbFade(E, Color(0,0,0), 10)
        skip(5)

        set(F, beatRed)
        rgbFade(F,red, 10)
        skip(5)

        //Beat 3
        rgbFade(C, bloodRed, 10)
        rgbFade(G, red, 10)
        rgbFade(F, red, 10)
        rgbFade(allBulbsGroup, offWhite, 10)
        skip(10)

        rgbFade(C, beatRed, 10)
        skip(10)
        set(C, bloodRed)

        set(B, beatRed)
        set(D, beatRed)
        rgbFade(B, Color(0,0,0), 10)
        rgbFade(D, Color(0,0,0), 10)
        skip(5)

        set(A, beatRed)
        set(E, beatRed)
        rgbFade(A,red, 10)
        rgbFade(E, red, 10)
        skip(5)

        //Beat 4

        rgbFade(E, red, 10)
        rgbFade(A, red, 10)
        rgbFade(G, red, 10)
        rgbFade(F, red, 10)
        rgbFade(C, bloodRed, 10)
        rgbFade(allBulbsGroup, offWhite, 10)
        skip(10)

        rgbFade(C, beatRed, 10)
        skip(10)
        set(C, bloodRed)

        set(B, beatRed)
        set(D, beatRed)
        rgbFade(B,red, 10)
        rgbFade(D,red,5)
        skip(5)

        //Beat 5 -> Sparkleboi
        rgbFade(C, beatRed, 10)
        set(allBulbsGroup, red)
        rgbFade(allBulbsGroup, beatRed, 10)
        skip(10)

        val c = Color(255, 134, 0) // Neon green
        effect(Aurora(allBulbsGroup, c, 5, 6, 30, 0.0f))
        skip(100)

        rgbFade(allBulbsGroup, Color(0,0,0), 5)
        skip(5)

        //Word Flash
        val wordColor1: Color = Color(255,0,93)
        val wordColor2: Color = Color(40,200,70)
        val wordColor3: Color = Color(0,255,246)

        set(A, wordColor1)
        set(B, wordColor1)
        skip(10)

        set(allBulbsGroup, Color(0,0,0))
        set(C, wordColor2)
        set(D, wordColor2)
        set(E, wordColor2)
        skip(10)

        set(allBulbsGroup, Color(0,0,0))
        set(F, wordColor3)
        set(G, wordColor3)
        skip(10)

        set(allBulbsGroup, Color(0,0,0))
        set(A, wordColor1)
        set(B, wordColor1)
        skip(10)

        set(allBulbsGroup, Color(0,0,0))
        set(C, wordColor2)
        set(D, wordColor2)
        set(E, wordColor2)
        set(F, wordColor2)
        set(G, wordColor2)
        skip(10)

        set(allBulbsGroup, Color(0,0,0))
        next()
        set(allBulbsGroup, wordColor3)
        skip(15)
    }
}
