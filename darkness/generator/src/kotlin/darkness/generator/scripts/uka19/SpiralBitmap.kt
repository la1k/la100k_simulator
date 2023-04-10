package darkness.generator.scripts.uka19

import darkness.generator.api.effects.BitMap
import java.awt.Color

class SpiralBitmap : BaseScript() {
    override suspend fun run() {
        super.run()

	//
	// Make bitmap
	//
	val res_x: Int = 12
	val res_y: Int = 7
	var plane: BitMap = BitMap(allBulbs, res_x, res_y)
	effect(plane)

	//
	// Run effect
	//
	val colors = arrayOf(Color(255, 255, 255), Color(255, 0, 0), Color(0,128,255), Color(128,255,0), Color(255,0,255), Color(255,255,128), Color(255,128,0))
	for (c in colors) {
		for (y_px in 0 until (res_y+1)/2) {
			//
			// Going right on the top side
			//
			for (x_px in y_px until res_x-y_px) {
        			plane.setPixel(x_px, y_px, c)
        			skip(1)
			}
			//
			// Going down on the right side
			//
			for (y_px_down in y_px until res_y-y_px) {
        			plane.setPixel(res_x-(y_px+1), y_px_down, c)
        			skip(1)
			}
			//
			// Going left on the bottom
			//
			for (x_px in y_px until res_x-y_px) {
        			plane.setPixel(res_x-(x_px+1), res_y-(y_px+1), c)
        			skip(1)
			}
			//
			// Going up on the left side
			//
			for (y_px_up in y_px until res_y-(y_px+1)) {
        			plane.setPixel(y_px, res_y-(y_px_up+1), c)
        			skip(1)
			}
		}
	}
	for(i in 0..10)
	{
		plane.setBrightness((10-i).toFloat()/10.0f)
		skip(1)
	}
	plane.cancel()
    }
}
