package darkness.generator.scripts.uka17

import darkness.generator.api.effects.FanScroll
import java.awt.Color

class FanScrollDemo : BaseScript() {
    override suspend fun run() {
        super.run()

        for (bulb in allBulbsRGB)
            set(bulb, Color.RED)


        val fan = FanScroll(allBulbsRGB, 30, Color.GREEN)
        effect(fan)
        // Keep blinking for a couple of seconds

        for (bulb in allBulbsRGB)
            rgbFade(bulb, Color.BLUE, 90)

        skip(90)
        fan.cancel()

        for (bulb in allBulbsRGB)
            set(bulb, Color.RED)

        val fan2 = FanScroll(allBulbsRGB, 30, Color.GREEN, true, true, 2.0)
        effect(fan2)
        // Keep blinking for a couple of seconds

        for (bulb in allBulbsRGB)
            rgbFade(bulb, Color.BLUE, 90)

        skip(90)
        fan2.cancel()
    }
}
