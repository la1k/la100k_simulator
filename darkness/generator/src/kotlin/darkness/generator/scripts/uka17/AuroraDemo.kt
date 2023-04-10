package darkness.generator.scripts.uka17

import darkness.generator.api.effects.Aurora
import java.awt.Color

class AuroraDemo : BaseScript() {
    override suspend fun run() {
        super.run()

        val c = Color(57, 255, 20) // neon green

        effect(Aurora(mergedAllBulbs, c, 10, 4, 30, 0.0f))
    }
}
