package darkness.generator.scripts.uka21

import darkness.generator.api.effects.PointRainbow
import kotlin.math.sin

class SphericalRainbow : BaseScript() {
    override suspend fun run() {
        super.run()

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
        for (frame in 0 until 30 * 20) {
            val y = 0.7f + 0.5f * sin(frame * 0.03).toFloat()
            val x = 5.0f + 6f * sin(frame * 0.002).toFloat()
            pointRainbow.setCenterPos(x, y)
            next()
        }
        skip(40)

        pointRainbow.cancel()

        // Fade back to black
        for (frame in 1..30) {
            pointRainbow.brightness = 1.0f - 1.0f * frame / 30.0f
            next()
        }
    }
}
