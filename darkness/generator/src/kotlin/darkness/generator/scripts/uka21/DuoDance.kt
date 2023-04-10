package darkness.generator.scripts.uka21

import darkness.generator.api.effects.CW
import darkness.generator.api.BulbGroup
import java.awt.Color

class DuoDance : BaseScript() {
    override suspend fun run() {
        super.run()

        val group1 = listOf(A,B,C,D)
        val group1Bulbs = BulbGroup(group1.flatMap { letter -> letter.allBulbs })
        val nodisplayGroup = listOf(E,F)
        val nodisplayGroupBulbs = BulbGroup(nodisplayGroup.flatMap { letter -> letter.allBulbs })
        val group2 = listOf(G,H,I,J)
        val group2Bulbs = BulbGroup(group2.flatMap { letter -> letter.allBulbs })
        
        val warmWhite = Color(255, 197, 143)
        val skyBlue = Color(64, 156, 255)
        val warmYellow = Color(255, 196, 0)

        val startFargeListe = listOf(uke_blå, uke_gull, uke_lilla, uke_turkis, uke_rød, warmYellow)
        var colorlist = generateSequence { startFargeListe }.flatten().take((startFargeListe.size+6)*2).toList()
        val delay = 5
        val fade = 2
        for (i in 0..startFargeListe.size*2){

            rgbFade(group1Bulbs, colorlist[i], fade)
            skip(delay)

            //rgbFade(nodisplayGroupBulbs, Color.black, 10)
            rgbFade(group2Bulbs, colorlist[i+3], fade)
            skip(delay)
        }

        rgbFade(group1Bulbs, warmYellow, 60)
        skip(50)
        rgbFade(group2Bulbs, uke_rød, 40)
        skip(20)
        rgbFade(nodisplayGroupBulbs, uke_turkis, 40)
        skip(100)
        rgbFade(group1Bulbs, Color.black, 30)
        rgbFade(group2Bulbs, Color.black, 30)
        rgbFade(nodisplayGroupBulbs, Color.black, 30)


/* 
        rgbFade(allBulbsGroup, Color.black, 10)
        skip(5)
        rgbFade(group1Bulbs, warmWhite, 20)
        skip(40)
        rgbFade(group2Bulbs, skyBlue, 40)
        skip(40)
        rgbFade(group1Bulbs, warmYellow, 60)
        skip(50)
        rgbFade(group2Bulbs, Color.red, 40)
        skip(200)
        rgbFade(group1Bulbs, Color.black, 30)
        rgbFade(group2Bulbs, Color.black, 30)
*/
    }
}
