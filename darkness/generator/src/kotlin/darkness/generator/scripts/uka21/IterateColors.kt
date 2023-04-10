package darkness.generator.scripts.uka21

import java.awt.Color

class IterateColors : BaseScript() {
    override suspend fun run() {
        super.run()

        val colors = arrayOf(Color(255, 255, 255), Color(255, 0, 0), Color(0, 255, 0), Color(0, 0, 255))
        for (c in colors) {
            set(allBulbsGroup, c)
            skip(20)
        }
    }
}
