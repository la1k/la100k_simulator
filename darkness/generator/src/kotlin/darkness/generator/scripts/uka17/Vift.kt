package darkness.generator.scripts.uka17

import darkness.generator.api.effects.FanScroll
import java.awt.Color

class Vift : BaseScript() {
    override suspend fun run() {
        super.run()

        val c = Color(218, 165, 32)
        val t = 3
        val fade_ext = 7

        val fan_full1 = FanScroll(allBulbsRGB, 50, c, true, false, 1.0)
        effect(fan_full1)
        skip(100)
        fan_full1.cancel()

        for (i in 0..2) {
            val fan_l2r = FanScroll(allBulbsRGB, 50, c, false, false, 1.2)
            val fan_r2l = FanScroll(allBulbsRGB, 50, c, false, true, 1.2)

            effect(fan_l2r)
            skip(10)
            effect(fan_r2l)

            skip(40)
            fan_l2r.cancel()
            skip(10)
            fan_r2l.cancel()
        }

        val fan_full2 = FanScroll(allBulbsRGB, 50, c, true, true, 1.0)
        effect(fan_full2)
        skip(100)
        fan_full2.cancel()

        skip(10)
    }
}
