package darkness.generator.scripts.uka17

import java.awt.Color

class Spinner : BaseScript() {
    override suspend fun run() {
        super.run()

        val center = mergedAllBulbs.position

        var theta = -Math.PI
        val omega = .4
        val epsilon = .3
        val delta = .2f
        val frames = 200

        val onColor = Color(198, 61, 15)
        val offColor = Color.BLACK
        val centerColor = Color(126, 143, 124)

        for (i in 0 until frames) {
            for (bulb in mergedAllBulbs) {
                val x = bulb.position[0] - center[0]
                val y = bulb.position[1] - center[1]

                if (Math.abs(Math.atan2(y.toDouble(), x.toDouble()) - theta) < epsilon || Math.abs(Math.atan2((-y).toDouble(), (-x).toDouble()) - theta) < epsilon) {
                    set(bulb, onColor)
                } else {
                    set(bulb, offColor)
                }

                // If darn close to the center, set anyway
                if (Math.abs(x) < delta && Math.abs(y) < delta)
                    set(bulb, centerColor)
            }

            theta += omega

            if (theta > Math.PI)
                theta = -Math.PI

            next()
        }

        for (bulb in mergedAllBulbs)
            rgbFade(bulb, Color.BLACK, 10)

        skip(10)
    }
}
