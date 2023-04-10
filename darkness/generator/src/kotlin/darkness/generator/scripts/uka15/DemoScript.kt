package darkness.generator.scripts.uka15

import java.awt.Color

class DemoScript : BaseScript() {
    override suspend fun run() {
        super.run()

        for (letter in letters) {
            for (bulb in letter) {
                set(bulb, Color.GREEN)
                next()
            }
        }

        skip(24)

        for (i in 0..3) {
            merge(DemoSubScript())
            next()
        }

        skip(96)
    }
}
