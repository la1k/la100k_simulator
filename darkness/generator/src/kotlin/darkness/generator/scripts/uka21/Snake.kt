package darkness.generator.scripts.uka21

import darkness.generator.api.effects.PointRainbow
import kotlin.math.sin
import java.awt.Color

class Snake : BaseScript() {
    override suspend fun run() {
        super.run()

        rgbFade(allBulbsGroup, uke_lilla,10)
        skip(20)

        for (letter in letters) {
            set(letter, uke_turkis)
            skip(5)
        }

        rgbFade(allBulbsGroup, Color.black, 10)
        skip(20)
        var trail_length = 4
        for (i in 0 until allBulbs.size+trail_length*3) {
            var bulb_nr1 = i - trail_length*1 
            var bulb_nr2 = i - trail_length*2
            var bulb_nr3 = i - trail_length*3
            if (i >= 0 && i < allBulbs.size) {
                set(allBulbs[i], uke_gull)
            }
            if (bulb_nr1 >= 0 && bulb_nr1 < allBulbs.size) {
                set(allBulbs[bulb_nr1], uke_blå)
            }
            if (bulb_nr2 >= 0 && bulb_nr2 < allBulbs.size) {
                set(allBulbs[bulb_nr2], uke_gull)
            }
            if (bulb_nr3 >= 0 && bulb_nr3 < allBulbs.size) {
                set(allBulbs[bulb_nr3], uke_blå)
            }
            skip(1)
        }
        trail_length = 8
       for (i in allBulbs.size+trail_length*3-1 downTo 0-trail_length*3-1 ) {
            var bulb_nr1 = i + trail_length*1 
            var bulb_nr2 = i + trail_length*2
            var bulb_nr3 = i + trail_length*3
            if (i >= 0 && i < allBulbs.size) {
                set(allBulbs[i], uke_gull)
            }
            if (bulb_nr1 >= 0 && bulb_nr1 < allBulbs.size) {
                set(allBulbs[bulb_nr1], uke_rød)
            }
            if (bulb_nr2 >= 0 && bulb_nr2 < allBulbs.size) {
                set(allBulbs[bulb_nr2], uke_gull)
            }
            if (bulb_nr3 >= 0 && bulb_nr3 < allBulbs.size) {
                set(allBulbs[bulb_nr3], uke_lilla)
            }
            skip(1)
        }

        rgbFade(allBulbsGroup, uke_gull,5)
        skip(10)
        rgbFade(allBulbsGroup, uke_blå,5)
        skip(10)
        rgbFade(allBulbsGroup, uke_gull,5)
        skip(10)
        rgbFade(allBulbsGroup, uke_blå,5)
        skip(10)
        rgbFade(allBulbsGroup, Color.black, 50)
        

    }
}

