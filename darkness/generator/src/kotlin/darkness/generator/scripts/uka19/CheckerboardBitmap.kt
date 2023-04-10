package darkness.generator.scripts.uka19

import darkness.generator.api.effects.BitMap
import java.awt.Color

class CheckerboardBitmap : BaseScript() {
    override suspend fun run() {
        super.run()

	//
	// Make bitmap
	//
	val scroll_res_x: Int = 40	// pixels per rectangle
	val scroll_res_y: Int = 60
	val res_x: Int = 2		// number of rectangles in checkboard
	val res_y: Int = 2
	var plane: BitMap = BitMap(allBulbs, scroll_res_x*res_x, scroll_res_y*res_y)
	effect(plane)

	//
	// Draw the checkerboard into the bitmap
	//
	val c1: Color = Color(0, 128, 255)
	val c2: Color = Color(255, 255, 255)
	plane.fill(c1)
	for (y_px in 0 until res_y step 2) {
		for (x_px in 0 until res_x step 2) {
			plane.fillRectangle(x_px*scroll_res_x, y_px*scroll_res_y, scroll_res_x, scroll_res_y, c2)
		}
	}
	for (y_px in 1 until res_y step 2) {
		for (x_px in 1 until res_x step 2) {
			plane.fillRectangle(x_px*scroll_res_x, y_px*scroll_res_y, scroll_res_x, scroll_res_y, c2)
		}
	}
	
	//
	// Fade in
	//
	for(i in 0..10)
	{
		plane.setBrightness(i.toFloat()/10.0f)
		plane.setOffset(i+229, i+229)
		skip(1)
	}

	//
	// Scroll the bitmap
	//
	for(i in 0..239)
	{
		plane.setOffset(i, i)
		skip(1)
	}

	for(i in 0..10)
	{
		plane.setBrightness((10-i).toFloat()/10.0f)
		plane.setOffset(i, i)
		skip(1)
	}

	plane.cancel()
    }
}
