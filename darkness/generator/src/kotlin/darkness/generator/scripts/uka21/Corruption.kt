package darkness.generator.scripts.uka21

import darkness.generator.api.BulbSet
import java.awt.Color
import darkness.generator.api.BulbGroup


class Corruption : BaseScript() {
    override suspend fun run() {
        super.run()

        val band = 0.1f
        val color = Color(128, 0, 128)

        val razz = BulbGroup(A.allBulbs.plus(B.allBulbs).plus(C.allBulbs).plus(D.allBulbs))
        val atazz = BulbGroup(F.allBulbs.plus(G.allBulbs).plus(H.allBulbs).plus(I.allBulbs).plus(J.allBulbs))


        val razzEnd = razz.allBulbs.maxOf { it.position[0] }
        val razzStart = razz.allBulbs.minOf { it.position[0] }
        
        val atazzEnd = atazz.allBulbs.maxOf { it.position[0] }
        val atazzStart = atazz.allBulbs.minOf { it.position[0] }


        val step = (razzEnd - razzStart) / (3 * 20)
        val atazzStep = (atazzEnd - atazzStart) / (3 * 20)

        var x = razzStart
        var atazzX = atazzEnd


        while (x <= razzEnd) {
            for (bulb in razz.allBulbs) {
                val bulbx = bulb.position[0]
                if (bulbx <= x) {
                    set(bulb, color)
                } else {
                    set(bulb, 0, 0, 0)
                }
            }
            for (bulb in atazz.allBulbs) {
                val bulbx = bulb.position[0]
                if (bulbx >= x) {
                    set(bulb, color)
                } else {
                    set(bulb, 0, 0, 0)
                }
            }
            skip(1)
            x += step
            atazzX -= atazzStep      
        }
    

        skip(5 * 20)
    }
}
