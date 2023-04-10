package darkness.generator.scripts.uka13

import darkness.generator.api.BulbRGB

import java.awt.*

class DemoSubScript : DemoBaseScript() {
    override suspend fun run() {
        super.run()
        var previous: BulbRGB? = null
        for (bulb in O) {
            set(bulb, Color.RED)
            if (previous != null) {
                set(previous, Color.BLACK)
            }
            previous = bulb
            next()
        }
        set(previous!!, Color.BLACK)
    }
}
