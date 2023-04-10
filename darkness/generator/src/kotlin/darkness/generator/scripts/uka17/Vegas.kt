package darkness.generator.scripts.uka17

import darkness.generator.api.BulbGroup
import darkness.generator.api.BulbRGB
import darkness.generator.api.effects.Cycle
import darkness.generator.api.effects.FanScroll
import java.awt.Color
import java.util.*

class Vegas : BaseScript() {
    override suspend fun run() {
        super.run()

        ////// CAUTION //////
        // Ugly code ahead //
        ////// CAUTION //////

        // Number of partitions
        val numGroups = 3

        val lists = List<LinkedList<BulbRGB>>(numGroups) { LinkedList() }

        // Don't pollute
        run {
            var count = 0

            // TODO: Probably a smooth functional way to do this
            // Setup bulb groups for vegas sign digits
            for (digit in digits) {
                for (bulb in digit) {
                    lists[count].add(bulb)
                    ++count

                    if (count >= numGroups)
                        count = 0
                }
            }
        }

        val groups = lists.map { BulbGroup(it) }

        // Feel free to open your eyes now

        // I wish java had structs
        val COL_startBg = Color(88, 24, 69)
        val COL_endBg = Color(39, 41, 109)
        val COL_fan = Color(109, 128, 55)
        val COL_bulbOn = Color.WHITE
        val COL_bulbOff: Color? = null // Relinquish

        // Initial sign colour
        rgbFade(mergedAllBulbs, COL_startBg, 10)
        skip(10)

        // Vegas casino neon sign effect
        val cycle = Cycle(COL_bulbOn, COL_bulbOff, 3, groups)
        effect(cycle)

        // Scrolling fan, nice background indeed
        val fan = FanScroll(allBulbsRGB, 60, COL_fan, true, false, 2.0)
        effect(fan)

        for (i in 0..1) {

            // Fade background to another colour while all the other shit is running
            for (bulb in allBulbsRGB)
                rgbFade(bulb, COL_endBg, 120)

            // Yep yep yep
            skip(120)

            // Fade back
            for (bulb in allBulbsRGB)
                rgbFade(bulb, COL_startBg, 120)

            // Herp derp
            skip(120)
        }

        // Cleanup
        fan.cancel()
        cycle.cancel()

        // Fade out
        for (bulb in mergedAllBulbs)
            rgbFade(bulb, Color.BLACK, 10)

        // Wait for fade
        skip(10)
    }
}
