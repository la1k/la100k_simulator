package darkness.generator.scripts.uka19

import darkness.generator.api.BulbGroup
import darkness.generator.api.BulbRGB
import darkness.generator.api.effects.Aurora
import darkness.generator.api.effects.FanScroll
import java.awt.Color
import java.util.*

class Intro2: BaseScript() {
    override suspend fun run() {
        super.run()
        // Colors
        val gold = Color(255, 211, 94)

        effect(Aurora(allBulbsGroup, gold, 10, 4, 30, 0.5f))
        skip(201)

        for (bulb in allBulbsGroup) {
            rgbFade(bulb, gold, 10)
        }

        skip(5)

        for (y_block in 2 downTo 0) {
            for (x_block in -8..8) {
                for (bulb in allBulbs) {
                    val x = bulb.position[0]*2.0f
                    val y = bulb.position[1]*2.0f

                    if (x<x_block && y>y_block) {
                        set(bulb, Color.black)
                    }
                }
                skip(1)
            }
        }

        val fan2 = FanScroll(allBulbs, 30, Color.red, true, true, 2.0)
        effect(fan2)

        // Keep blinking for a couple of seconds
        for (bulb in allBulbsGroup) {
            rgbFade(bulb, Color.white, 90)
        }

        skip(90)
        fan2.cancel()
        for (bulb in allBulbsGroup) {
            set(bulb, Color.white)
        }
        skip(5)

        // Ombre
        val blå = Color(7, 0, 204)
        val rød = Color (179, 0, 41)
        val gul = Color(204, 190, 0)
        val oransj = Color(204, 105, 0)

        val startFargeListe = listOf(blå, rød, gul, oransj, Color.white)

        for (startColor in startFargeListe) {
            val hsvValues = Color.RGBtoHSB(startColor.red, startColor.green, startColor.blue, null)
            val hue = hsvValues[0]
            val brightness = hsvValues[2]
            val fargeListe = mutableListOf(startColor)
            for (i in 1..6) {
                val saturation = 1 - i/6.0f
                fargeListe.add(Color.getHSBColor(hue, saturation, brightness))
            }
            for (startPoint in 0 until letters.size) {
                var colorIndex = startPoint
                for (letter in letters.take(startPoint+1)) {
                    set(letter, fargeListe[colorIndex])
                    colorIndex = (colorIndex - 1 + letters.size) % letters.size
                }
                skip(5)
            }
        }

        for (bulb in allBulbsGroup) {
            rgbFade(bulb, Color.white, 10)
        }
        skip(20)


        val centerHeart = bulb(27)
        var r = 0.17
        var r_ = -0.03
        val w = 0.85
        val r_heart = 0.5

        val frames = 100

        // Red
        val strong = Color(255, 0, 0)
        val c = Color(200, 0, 0 )
        val dim = Color(100, 0 , 0)
        val off = Color(255, 255, 255)

        val largest_dist = dist_bulbs(bulb(61), centerHeart)+2*w

        set(C, dim)
        skip(2)
        set(C, c)
        skip(5)
        set(C, dim)
        skip(4)
        set(C, c)
        skip(4)
        set(C, strong)

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
                        set(bulb, Color.white)
                    }
                    else
                        set(bulb, dim)
                }
            }
            r += 0.4
            r_+= 0.4
            skip(1)

            if(r > largest_dist){
                r = 0.17
                /*set(C, dim)
                skip(6)
                set(C, strong)
                skip(2)*/
                set(C, dim)
                skip(2)
                set(C, c)
                skip(5)
                set(C, dim)
                skip(4)
                first = false
            }
            if (r_  > largest_dist) {
                r_ = -0.03
                /*set(C, 50, 0, 0)
                skip(6)
                set(C, dim)
                skip(2)*/
                set(C, c)
                skip(4)
                set(C, strong)
                //skip(4)
            }

        }

        skip(1)
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
