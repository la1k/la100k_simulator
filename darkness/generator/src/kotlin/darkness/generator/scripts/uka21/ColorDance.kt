package darkness.generator.scripts.uka21

import darkness.generator.api.effects.CW
import darkness.generator.api.BulbGroup
import java.awt.Color

class ColorDance : BaseScript() {
    override suspend fun run() {
        super.run()

        val group1 = listOf(A,B,C,D)
        val group1Bulbs = BulbGroup(group1.flatMap { letter -> letter.allBulbs })
        val nodisplayGroup = listOf(E,F)
        val nodisplayGroupBulbs = BulbGroup(nodisplayGroup.flatMap { letter -> letter.allBulbs })
        val group2 = listOf(G,H,I,J)
        val group2Bulbs = BulbGroup(group2.flatMap { letter -> letter.allBulbs })

        val startFargeListe = listOf(uke_blå, uke_gull, uke_lilla, uke_rød, uke_turkis)
        var colorlist = generateSequence { startFargeListe }.flatten().take(startFargeListe.size+6).toList()
        val delay = 10
        val fade = 5
        for (i in 0..startFargeListe.size-1){

            rgbFade(group1Bulbs, colorlist[i+2], fade)
            skip(delay)

            rgbFade(nodisplayGroupBulbs, colorlist[i], fade)
            skip(delay)

            rgbFade(group2Bulbs, colorlist[i+3], fade)
            skip(delay)
        }
    }
}
