package darkness.generator.scripts.uka15

import darkness.generator.api.effects.PointRainbow

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
            val y = 0.7f + 0.5f * Math.sin((frame * 0.03f).toDouble()).toFloat()
            val x = 5.0f + 6f * Math.sin((frame * 0.002f).toDouble()).toFloat()
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
