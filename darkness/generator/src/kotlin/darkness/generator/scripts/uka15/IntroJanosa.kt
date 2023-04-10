package darkness.generator.scripts.uka15

import darkness.generator.api.BulbManager
import darkness.generator.api.effects.BlinkyPhase
import java.awt.Color
import java.util.*

class IntroJanosa : BaseScript() {
    override suspend fun run() {
        super.run()

        /*MiniLysreklamen
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
        for (frameCount in 0 until 9 * 20) {
            for (bulb in BulbManager.allBulbs) {
                val colors = Color.RGBtoHSB(bulb.red, bulb.green, bulb.blue, null)


                setHSB(bulb, colors[0], 1.0f, 0.8f + (0.2f * Math.sin((frameCount.toFloat() * 0.1f + bulb.position[0] / 2.5f).toDouble())).toFloat())
            }
            next()
        }
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

        val fadeInOrder = listOf(C, F, A, D, G, E, B)

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
