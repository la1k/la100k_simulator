package darkness.generator.scripts.uka15

import darkness.generator.api.BulbRGB

class DemoSubScript : BaseScript() {
    override suspend fun run() {
        super.run()
        var previous: BulbRGB? = null
        for (letter in letters) {
            for (bulb in letter) {
                if (previous != null) {
                    relinquish(previous)
                }
                set(bulb, 255, 0, 0)
                previous = bulb
                next()
            }
        }
        relinquish(previous!!)
    }
}
