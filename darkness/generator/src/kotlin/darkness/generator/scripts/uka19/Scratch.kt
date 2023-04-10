package darkness.generator.scripts.uka19

import java.awt.Color

class Scratch : BaseScript() {
    override suspend fun run() {
        super.run()

	val colors = arrayOf(Color(255, 255, 255), Color(255, 0, 0), Color(0,128,255), Color(128,255,0), Color(255,0,255), Color(255,255,128), Color(255,128,0))

	for (c in colors) {
	for (y_block in 2 downTo 0) {
		for (x_block in -8..8) {
        		for (bulb in allBulbs) {
                		val x = bulb.position[0]*2.0f
                		val y = bulb.position[1]*2.0f

                		if (x<x_block && y>y_block) {
                    			set(bulb, c)
                		}
			}
        		skip(1)
		}
	}
	}
    }
}
