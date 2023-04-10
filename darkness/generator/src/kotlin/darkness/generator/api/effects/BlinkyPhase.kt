package darkness.generator.api.effects

import darkness.generator.api.BulbManager
import darkness.generator.api.BulbRGB

import java.util.LinkedList
import java.util.Random

class BlinkyPhase : EffectBase() {
    // Pseudorandom with fixed seed. Can be changed to get a different, but similar effect
    private val random = Random(1337)

    override suspend fun run() {
        val freeBulbs = LinkedList<BulbRGB>(BulbManager.allBulbs)
        while (!isCancelled && !freeBulbs.isEmpty()) {
            val bulbIndex = random.nextInt(freeBulbs.size)
            val bulb = freeBulbs[bulbIndex]
            if (bulb.red != 0 || bulb.green != 0 || bulb.blue != 0) {
                // This bulb is in use. Lets find a new one
                freeBulbs.removeAt(bulbIndex)
                continue
            }
            setHSB(bulb, random.nextFloat(), 0.6f, 0.7f)
            val color = bulb.color
            next()
            // The color has not changed by another effect. Turn it off
            if (bulb.color == color) {
                set(bulb, 0, 0, 0)
            }
        }
    }

    override fun toString() = "BlinkyPhase"
}
