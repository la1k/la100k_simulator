package darkness.generator.scripts.uka21

import darkness.generator.api.effects.CW
import darkness.generator.api.BulbGroup
import java.awt.Color
import java.util.Random

class LetterColorDance : BaseScript() {
    override suspend fun run() {
        super.run()
        val rnd = Random(1337)
        val group1 = listOf(A,B,C,D)
        val group1Bulbs = BulbGroup(group1.flatMap { letter -> letter.allBulbs })
        val nodisplayGroup = listOf(E,F)
        val nodisplayGroupBulbs = BulbGroup(nodisplayGroup.flatMap { letter -> letter.allBulbs })
        val group2 = listOf(G,H,I,J)
        val group2Bulbs = BulbGroup(group2.flatMap { letter -> letter.allBulbs })

        val startFargeListe = listOf(uke_blå, uke_gull, uke_lilla, uke_rød, uke_turkis)
        var colorlist = generateSequence { startFargeListe }.flatten().take(startFargeListe.size+6).toList()
        val delay = 5
        val fade = 2
        for (i in 0..startFargeListe.size-1){

            for (letter in letters) {
                var old_idx = 10
                var new_idx = rnd.nextInt(startFargeListe.size)
                while(old_idx == new_idx) {
                    new_idx = rnd.nextInt(startFargeListe.size)
                }
                old_idx = new_idx
                rgbFade(letter, startFargeListe[new_idx], fade)
                skip(delay)
            }
        }
        skip(10)
        rgbFade(allBulbsGroup, Color.black, 10)
    }
}
