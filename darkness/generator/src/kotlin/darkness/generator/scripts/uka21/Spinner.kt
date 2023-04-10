package darkness.generator.scripts.uka21

import java.awt.Color
import kotlin.math.abs
import kotlin.math.atan2

class Spinner : BaseScript() {
    override suspend fun run() {
        super.run()

        val center = allBulbsGroup.position
        center[1] += 0.2f // Avoid getting two center bulbs

        var theta = -Math.PI
        val omega = .4
        val epsilon = .3
        val delta = .1f
        val frames = 200

        val onColor = Color(198, 61, 15)
        val offColor = Color.BLACK
        val centerColor = Color(126, 143, 124)

        for (i in 0 until frames) {
            for (bulb in allBulbs) {
                val x = (bulb.position[0] - center[0]).toDouble()
                val y = (bulb.position[1] - center[1]).toDouble()

                if (abs(atan2(y, x) - theta) < epsilon || abs(atan2(-y, -x) - theta) < epsilon) {
                    set(bulb, onColor)
                } else {
                    set(bulb, offColor)
                }

                // If darn close to the center, set anyway
                if (abs(x) < delta && abs(y) < delta) {
                    set(bulb, centerColor)
                }
            }

            theta += omega

            if (theta > Math.PI)
                theta = -Math.PI

            next()
        }

        for (bulb in allBulbs) {
            rgbFade(bulb, Color.BLACK, 10)
        }

        skip(10)
    }
}
