package darkness.generator.scripts.uka13

import java.awt.Color

class DemoScript : DemoBaseScript() {
    override suspend fun run() {
        super.run() // Must call super if extending a custom base script

        val rainbowColors = listOf(Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN, Color(0, 255, 127), Color.BLUE, Color(127, 0, 255), Color(255, 0, 255))

        // Make each letter fade in, with a little delay between the start of each letter and a separate color for each letter
        for (i in 0 until letters.size) {
            rgbFade(letters[i], rainbowColors[i], 48)
            skip(12)
        }
        skip(36)

        // Turn each bulb white, one per frame
        for (letter in letters) {
            for (bulb in letter) {
                set(bulb, Color.WHITE)
                next()
            }
        }

        // Start the same sub-script three times. Note that with the current implementation, a bulb that is turned off
        // by one script cannot in the same frame be turned on by another; hence the skip.
        for (i in 0..2) {
            merge(DemoSubScript())
            skip(2)
        }

        // Enjoy the results for four seconds
        skip(96)
    }
}
