package darkness.generator.api.effects

import darkness.generator.api.BulbRGB
import java.awt.Color

/**
 * Bitmap plane
 * 
 * Make sure the physical measures enclose the bulb-group you want affected
 *
 */
class BitMap(
    bulbs: Collection<BulbRGB>,
    	// in pixels
    private val resolution_x: Int,
    private val resolution_y: Int,
    	// in meter. X = 0 and Y = 0 is lower left corner
    private var physical_x: Float = 1000.0f,
    private var physical_y: Float = 1000.0f,
    private var physical_w: Float = 0.0f,
    private var physical_h: Float = 0.0f
) : EffectBase() {
    private var bulbs: List<BulbRGB> = bulbs.toList()
    private var bitMapPlane: Array<Array<Color>>
    private var offset_x: Int = 0
    private var offset_y: Int = 0

    private var brightness: Float = 1.0f


    init {
	bitMapPlane = Array(resolution_y) {Array(resolution_x) {Color(0,0,0)}}

	//
	// Set default bounds based on the size of the group, unless spesific bounds are provided
	//
	var x_left: Float = 1000.0f
	var x_right: Float = -1000.0f
	var y_top: Float = -1000.0f
	var y_bottom: Float = 1000.0f
	for (bulb in bulbs) {
		if(bulb.position[0] < x_left)
		{
			x_left = bulb.position[0];
		}
		if(bulb.position[0] > x_right)
		{
			x_right = bulb.position[0];
		}
		if(bulb.position[1] < y_bottom)
		{
			y_bottom = bulb.position[1];
		}
		if(bulb.position[1] > y_top)
		{
			y_top = bulb.position[1];
		}
	}

	if(physical_x > 999.0f)
	{
		physical_x = x_left
	}
	if(physical_w < 0.1f)
	{
		physical_w = 0.1f+x_right-x_left
	}
	if(physical_y > 999.0f)
	{
		physical_y = y_bottom
	}
	if(physical_h < 0.1f)
	{
		physical_h = 0.1f+y_top-y_bottom
	}
    }

    override suspend fun run() {
        while (!isCancelled) {
		for (bulb in bulbs) {
			val posX: Float = bulb.position[0]
			val posY: Float = bulb.position[1]
	
			if(posX >= physical_x && posX < physical_x+physical_w && posY >= physical_y && posY < physical_y+physical_h)
			{
				val pixelX: Int = ((resolution_x*(posX-physical_x))/physical_w).toInt()
				val pixelY: Int = ((resolution_y*(posY-physical_y))/physical_h).toInt()
				var actual_pixelX: Int = Math.floorMod(offset_x+pixelX, resolution_x)
				var actual_pixelY: Int = Math.floorMod(offset_y+resolution_y-(pixelY+1), resolution_y)
				val c: Color = bitMapPlane[actual_pixelY][actual_pixelX]
				val hsbValues = Color.RGBtoHSB(c.red, c.green, c.blue, null)
				set(bulb, Color.getHSBColor(hsbValues[0], hsbValues[1], hsbValues[2]*brightness))
			}
        	}
        	skip(1)
	}
    }

    //
    // These values detemine which pixel in the bitmap is displayed in the top left corner of the projection
    //
    // Gradually change these over time to scroll the bitmap projection around. It will wrap at the edges.
    //
    fun setOffset(ox: Int, oy: Int) {
	offset_x = ox
	offset_y = oy
    }

    fun setBrightness(b: Float) {
	brightness = b
    }


    //
    // Drawing-functions
    //

    fun fill(r: Int, g: Int, b: Int) {
	fill(Color(r,g,b))
    }

    fun fill(c: Color) {
	fillRectangle(0, 0, resolution_x, resolution_y, c)
    }

    fun fillRectangle(x: Int, y: Int, w: Int, h: Int, r: Int, g: Int, b: Int) {
	fillRectangle(x, y, w, h, Color(r,g,b))
    }

    fun fillRectangle(x: Int, y: Int, w: Int, h: Int, c: Color) {
	for(px_x in x until x+w)
	{
		for(px_y in y until y+h)
		{
			if(px_x<resolution_x && px_y<resolution_y)
			{
				bitMapPlane[px_y][px_x] = c
			}
		}
	}
    }

    fun setPixel(x: Int, y: Int, r: Int, g: Int, b: Int) {
	setPixel(x, y, Color(r,g,b))
    }

    fun setPixel(x: Int, y: Int, c: Color) {
	if(x<resolution_x && y<resolution_y)
	{
		bitMapPlane[y][x] = c
	}
    }

    override fun toString() = "I see you are a man of culture!"
}
