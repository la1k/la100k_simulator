package darkness.generator.scripts.uka21

import darkness.generator.api.BulbGroup
import darkness.generator.api.BulbRGB
import darkness.generator.api.effects.Aurora
import darkness.generator.api.effects.FanScroll
import java.awt.Color

class Heart : BaseScript() {
    override suspend fun run() {
        super.run()
        val centerHeart = bulb(55)
        var r = 0.17
        var r_ = -0.03
        val w = 1.05
        val r_heart = 0.4

        val frames = 100

        // Red
        val strong = Color(229, 1, 1)
        val c = Color(179, 1, 1)
        val dim = Color(79, 1, 1)
        val off = Color(255, 255, 255)

        val largest_dist = dist_bulbs(bulb(103), centerHeart)+2*w

        set(E, dim)
        skip(2)
        set(E, c)
        skip(5)
        set(E, dim)
        skip(4)
        set(E, c)
        skip(4)
        set(E, strong)

        var first = true
        for (i in 0 until frames) {
            for (bulb in allBulbs) {
                // Calculate distance from bulb to center
                var dist = dist_bulbs(bulb, centerHeart)

                // If bulb is inside circle, turn on
                if((dist > r && dist < r.plus(w)) || (dist > r_ && dist < r_.plus(w))|| dist < r_heart){
                    set(bulb, c)
                }
                else {
                    if(first && dist > r.plus(w)){
                        set(bulb, Color.black)
                    }
                    else
                        set(bulb, dim)
                }
            }
            r += 0.5
            r_+= 0.5
            skip(1)

            if(r > largest_dist){
                r = 0.17
                /*set(C, dim)
                skip(6)
                set(C, strong)
                skip(2)*/
                set(E, dim)
                skip(2)
                set(E, c)
                skip(5)
                set(E, dim)
                skip(4)
                first = false
            }
            if (r_  > largest_dist) {
                r_ = -0.03
                /*set(C, 50, 0, 0)
                skip(6)
                set(C, dim)
                skip(2)*/
                set(E, c)
                skip(4)
                set(E, strong)
                //skip(4)
            }

        }

        skip(1)
        for (bulb in allBulbsGroup) {
            rgbFade(bulb, Color.black, 10)
        }
    }

    fun dist_bulbs(bulb_1: BulbRGB, bulb_2: BulbRGB):Double{
        val x_1 = bulb_1.position[0].toDouble()
        val y_1 = bulb_2.position[1].toDouble()

        val x_2 = bulb_2.position[0].toDouble()
        val y_2 = bulb_2.position[1].toDouble()

        val dist_x = (x_2.minus(x_1))*(x_2.minus(x_1))
        val dist_y = (y_2.minus(y_1))*(y_2.minus(y_1))

        val dist = Math.sqrt(dist_x.plus(dist_y))

        return dist
    }
}
