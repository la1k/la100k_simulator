package darkness.generator.scripts.uka21

import darkness.generator.api.effects.Aurora
import java.awt.Color

class AuroraDemo : BaseScript() {
    override suspend fun run() {
        super.run()
        //val c = Color(57, 255, 20) // Neon green
        val c = uke_gull
        effect(Aurora(allBulbsGroup, c, 10, 4, 30, 0.0f))
        skip(201)

        for (bulb in allBulbsGroup) {
            rgbFade(bulb, Color.black, 10)
        }

        skip(5)
    }
}