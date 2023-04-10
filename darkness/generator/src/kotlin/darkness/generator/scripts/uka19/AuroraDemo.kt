package darkness.generator.scripts.uka19

import darkness.generator.api.effects.Aurora
import java.awt.Color

class AuroraDemo : BaseScript() {
    override suspend fun run() {
        super.run()
        val c = Color(57, 255, 20) // Neon green
        effect(Aurora(allBulbsGroup, c, 10, 4, 30, 0.0f))
    }
}
