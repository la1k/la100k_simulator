package darkness.generator.scripts.uka17

import darkness.generator.api.BulbRGB
import java.awt.Color
import java.util.*

class WaterFill : BaseScript() {
    override suspend fun run() {
        super.run()

        val c = Color(0, 127, 255)
        val t = 3
        val fade_ext = 7

        var positionBuffer: FloatArray
        var orderedBulbs: SortedMap<Float, ArrayList<BulbRGB>>
        for (letter in letters) {
            orderedBulbs = TreeMap()
            var bulbList: ArrayList<BulbRGB>
            for (bulb in letter) {
                positionBuffer = bulb.position
                bulbList = (orderedBulbs as java.util.Map<Float, ArrayList<BulbRGB>>).getOrDefault(positionBuffer[1], ArrayList())
                bulbList.add(bulb)
                orderedBulbs[positionBuffer[1]] = bulbList
            }

            var lastHeight: Float
            for ((key, value) in orderedBulbs) {
                for (bulb in value) {
                    rgbFade(bulb, c, t + fade_ext)
                }
                skip(t)
                lastHeight = key
            }
            // TODO: filling drops through thingy missing
            skip(fade_ext)
        }
        skip(10)
    }
}
