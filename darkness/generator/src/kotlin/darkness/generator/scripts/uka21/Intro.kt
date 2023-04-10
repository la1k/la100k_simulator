package darkness.generator.scripts.uka21

import darkness.generator.api.BulbGroup
import darkness.generator.api.BulbRGB
import darkness.generator.api.effects.Aurora
import darkness.generator.api.effects.FanScroll
import darkness.generator.api.effects.PointRainbow

import kotlin.math.sin
import java.awt.Color
import java.util.*

class Intro: BaseScript() {
    override suspend fun run() {
        super.run()
        // Colors

        effect(Aurora(allBulbsGroup, uke_gull, 10, 4, 30, 0.5f))
        skip(201)

        for (bulb in allBulbsGroup) {
            rgbFade(bulb, uke_gull, 10)
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

        val fan2 = FanScroll(allBulbs, 30, uke_rød, true, true, 2.0)
        effect(fan2)

        // Keep blinking for a couple of seconds
        for (bulb in allBulbsGroup) {
            rgbFade(bulb, Color.black, 90)
        }

        skip(90)
        fan2.cancel()
        for (bulb in allBulbsGroup) {
            set(bulb, Color.black)
        }
        skip(5)

        // Spherical rainbow

        val pointRainbow = PointRainbow(allBulbs, floatArrayOf(5.0f, 0.7f), 4f)
        // Turn off the brightness, and start fading in
        pointRainbow.brightness = 0.0f
        effect(pointRainbow)

        next()
        for (frame in 1..30) {
            pointRainbow.brightness = 1.0f * frame / 30.0f
            next()
        }

        // Start moving the center point around
        for (frame in 0 until 30 * 5) {
            val y = 0.7f + 0.5f * sin(frame * 0.3).toFloat()
            val x = 5.0f + 6f * sin(frame * 0.02).toFloat()
            pointRainbow.setCenterPos(x, y)
            next()
        }

        // Fade back to black
        for (frame in 1..10) {
            pointRainbow.brightness = 1.0f - 1.0f * frame / 10.0f
            next()
        }

        pointRainbow.cancel()

        set(allBulbsGroup, Color.black)
        skip(10)

        /* Call onother script sequential */
        var obj1 = ColorDance()
        merge(obj1)
        while(obj1.isRunning){
            next()
        }
        skip(10)
        /* Call onother script sequential */
        var obj2 = Snake()
        merge(obj2)
        while(obj2.isRunning){
            next()
        }

        skip(10)
        /* Call onother script sequential */
        var obj3 = DuoDance()
        merge(obj3)
        while(obj3.isRunning){
            next()
        }

    }

}
