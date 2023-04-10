package darkness.generator.scripts.uka17

import darkness.generator.api.BulbRGB

class Iterate : BaseScript() {
    override suspend fun run() {
        super.run()
        var previousBulb: BulbRGB? = null
        for (letter in letters) {
            for (bulb in letter) {
                set(bulb, 255, 255, 255)
                if (previousBulb != null) {
                    set(previousBulb, 0, 0, 0)
                }
                previousBulb = bulb
                skip(5)
            }
        }
    }
}
