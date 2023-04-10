package darkness.generator.scripts.uka21

import darkness.generator.api.effects.BitMap
import java.awt.Color
import java.lang.Math

class NorskFlaggBitmap : BaseScript() {
    override suspend fun run() {
        super.run()

	val start_angular_time: Int = 20	// Use this to set the starting phase of the waveyness
	val offset_x: Int = -8			// Use this to place the vertical bar of the cross, number of sub-pixels the bitmap is scrolled to the left
	val wavey_x: Float = 1.0f		// Use this to define horizontal waveyness (number of pixels to stray left/right from initial position)
	val wavey_y: Float = 1.0f		// Use this to define vertical waveyness (number of pixels to stray up/down from initial position)

	//
	// Make bitmap
	//
	val res_x: Int = 44		// proper flagg is 22x16, but lets just make the tail end twice as long to avoid having to stretch out the horizontal part to cover everything
	val res_y: Int = 16
	val sub_res: Int = 10		// sub-resolution, pixels-per-pixel, gives us higher actual resolution for smoother scrolling-effect.
					// Set projection plane to define desired zoom. Bigger plane means more zoomed-in. Plane should keep aspect-ratio of bitmap. 44x16 => 10m x 3.64m.
	var plane: BitMap = BitMap(allBulbs, res_x*sub_res, res_y*sub_res, -3.2f, -1.15f, 10.0f, 3.64f)
	effect(plane)
	plane.setOffset(offset_x + Math.round(wavey_x*(sub_res.toFloat())*Math.sin((start_angular_time.toDouble())/30.0f)).toInt(), Math.round(wavey_y*(sub_res.toFloat())*Math.cos((start_angular_time.toDouble())/13.0f)).toInt())
	
	//
	// Draw Norwegian flag, according to official specifications
	//
	plane.fill(Color(0xBA, 0x12, 0x2B))
	plane.fillRectangle(0, 6*sub_res, res_x*sub_res, 4*sub_res, Color(0xFF, 0xFF, 0xFF))
	plane.fillRectangle(6*sub_res, 0, 4*sub_res, res_y*sub_res, Color(0xFF, 0xFF, 0xFF))
	plane.fillRectangle(0, 7*sub_res, res_x*sub_res, 2*sub_res, Color(0x00, 0x24, 0x69))
	plane.fillRectangle(7*sub_res, 0, 2*sub_res, res_y*sub_res, Color(0x00, 0x24, 0x69))	

	//
	// Fade in
	//
	for(i in 0..10)
	{
		plane.setBrightness((i).toFloat()/10.0f)
		skip(1)
	}	

	//
	// Wave flag
	//
	for(i in start_angular_time until 520)
	{
		plane.setOffset(offset_x + Math.round(wavey_x*(sub_res.toFloat())*Math.sin((i.toDouble())/30.0f)).toInt(), Math.round(wavey_y*(sub_res.toFloat())*Math.cos((i.toDouble())/13.0f)).toInt())
		skip(1)
	}

	//
	// Fade out
	//
	for(i in 0..10)
	{
		plane.setBrightness((10-i).toFloat()/10.0f)
		skip(1)
	}
	plane.cancel()
    }
}
