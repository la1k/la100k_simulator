package darkness.generator.scripts.uka19

import darkness.generator.api.effects.FanScroll
import java.awt.Color

class FanScrollDemo : BaseScript() {
    override suspend fun run() {
        super.run()

        for (bulb in allBulbsGroup) {
            set(bulb, Color.RED)
        }

        val fan = FanScroll(allBulbs, 30, Color.GREEN)
        effect(fan)

        // Keep blinking for a couple of seconds
        for (bulb in allBulbsGroup) {
            rgbFade(bulb, Color.BLUE, 90)
        }

        skip(90)
        fan.cancel()

        for (bulb in allBulbsGroup) {
            set(bulb, Color.RED)
        }

        val fan2 = FanScroll(allBulbs, 30, Color.GREEN, true, true, 2.0)
        effect(fan2)

        // Keep blinking for a couple of seconds
        for (bulb in allBulbsGroup) {
            rgbFade(bulb, Color.BLUE, 90)
        }

        skip(90)
        fan2.cancel()
    }
}
