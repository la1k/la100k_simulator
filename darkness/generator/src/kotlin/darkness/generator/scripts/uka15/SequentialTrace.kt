package darkness.generator.scripts.uka15

import java.awt.Color

class SequentialTrace : BaseScript() {
    private var warmWhite = Color(255, 197, 143)

    override suspend fun run() {
        super.run()
        turnOnSequentially()
    }

    private suspend fun turnOnSequentially() {
        // Turn each bulb on one by one
        for (letter in letters) {
            for (bulb in letter) {
                set(bulb, warmWhite)
                next()
            }
        }

        for (letter in letters) {
            for (bulb in letter) {
                set(bulb, Color.black)
                next()
            }
        }
    }
}
