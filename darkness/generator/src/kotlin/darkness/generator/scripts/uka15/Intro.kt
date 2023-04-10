package darkness.generator.scripts.uka15

import darkness.generator.api.BulbManager
import darkness.generator.api.effects.BlinkyPhase
import darkness.generator.api.effects.PointRainbow

import java.awt.*
import java.util.LinkedList
import java.util.Random

class Intro : BaseScript() {
    override suspend fun run() {
        super.run()

        /* MiniLysreklamen
		Phase 1, blink bulbs randomly
		Phase 2: for each letter fill up with one color.
				Then increase the intensity, and decrease it again. This will make a glowing effect
		Phase 3: Scroll the current colors out of the sign to right
		Phase 4: Fade the whole sign in one color
		Phase 5: Fade out to black
		Phase 6: Phase one and one letter in
		Phase 7: Morph into a rainbow
		Phase 8: Do the rainbow dance
		Phase 9: Fade back to the color in phase 4
		Phase 10: Fade to black and exit
		*/

        // Start phase 1 in a parallel.
        val blinky = BlinkyPhase()
        effect(blinky)
        // Keep blinking for a couple of seconds
        skip(30)

        // Start fading in letters
        fadeIn()
        blinky.cancel()

        skip(1 * 20)

        // Glow
        glow()

        bloomToRainbow()

        // Scroll out
        scrollOut()

        skip(10)

        // Fade to color

        for (letter in letters) {
            rgbFade(letter, 255, 147, 41, 10)
        }

        skip(20)

        for (letter in letters) {
            rgbFade(letter, 0, 0, 0, 20)
        }

        skip(40)


    }

    private suspend fun glow() {
        for (frameCount in 0 until 10 * 20) {
            for (bulb in BulbManager.allBulbs) {
                val colors = Color.RGBtoHSB(bulb.red, bulb.green, bulb.blue, null)


                setHSB(bulb, colors[0], 1.0f, 0.8f + (0.2f * Math.sin((frameCount.toFloat() * 0.2f + bulb.position[0] / 2.5f).toDouble())).toFloat())
            }
            next()
        }
    }

    private suspend fun bloomToRainbow() {
        // Via white to 3d rainbow
        for (bulb in BulbManager.allBulbs) {
            rgbFade(bulb, 255, 255, 255, 10)
        }
        skip(10)

        val pointRainbow = PointRainbow(allBulbs, floatArrayOf(5.0f, 0.7f), 4f)
        //effect(pointRainbow);

        // Fade to rainbow /HACKHACK

        val centerPos = floatArrayOf(5.0f, 0.7f)
        val radius = 4.0f

        for (bulb in BulbManager.allBulbs) {
            // calculate the distance
            val pos = bulb.position

            val distance = Math.sqrt(((centerPos[0] - pos[0]) * (centerPos[0] - pos[0]) + (centerPos[1] - pos[1]) * (centerPos[1] - pos[1])).toDouble()).toFloat()

            val colorAngle = distance / radius % 1.0f // It's actually possible to do modulus on floating points in java :o
            hsbFade(bulb, colorAngle, 1.0f, 1.0f, 10)

        }

        skip(10)

        // Start the actual effect
        effect(pointRainbow)

        // Start moving the center point around
        for (frame in 0 until 15 * 20) {
            val y = 0.7f + 0.5f * Math.sin((frame * 0.03f).toDouble()).toFloat()
            val x = 5.0f + 6f * Math.sin((frame * 0.002f).toDouble()).toFloat()
            pointRainbow.setCenterPos(x, y)
            next()
        }

        pointRainbow.cancel()
    }

    private suspend fun scrollOut() {
        for (iteration in columns.indices) {
            for (column in columns.indices.reversed()) {
                val preColumn = column - 1
                var preColumnColor = Color.black
                if (preColumn >= 0) {
                    // Valid colums
                    preColumnColor = columns[preColumn].getBulb(0).color
                }
                for (bulb in columns[column]) {
                    set(bulb, preColumnColor)
                }
            }
            next()
        }
    }

    private suspend fun fadeIn() {
        val random = Random(1)
        val fadeInOrder = listOf(C, F, B, D, A, E, G)

        for (idx in fadeInOrder.indices) {
            val letter = fadeInOrder[idx]
            // Bulbs
            val freeBulbs = LinkedList(letter.allBulbs)
            // Get a color for our letter
            val colorAngle = idx.toFloat() / fadeInOrder.size.toFloat()

            var i = 0
            while (!freeBulbs.isEmpty()) {
                val bulbIndex = random.nextInt(freeBulbs.size)
                val bulb = freeBulbs[bulbIndex]
                setHSB(bulb, colorAngle, 1f, 0.6f)
                freeBulbs.removeAt(bulbIndex)
                if (++i % 2 == 0)
                    skip(1)
            }

            hsbFade(letter, floatArrayOf(colorAngle, 1f, 1.0f), 4)
            //rgbFade(letter, 255, 255, 255, 2);
            //hsbFade(letter, new float[]{colorAngle, 1.0f, 1.0f}, 4);
            skip(4)
            hsbFade(letter, floatArrayOf(colorAngle, 1f, 0.8f), 4)
            skip(4)
        }
    }
}
