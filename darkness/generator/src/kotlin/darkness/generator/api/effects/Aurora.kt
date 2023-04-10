package darkness.generator.api.effects

import darkness.generator.api.BulbGroup

import java.awt.*
import java.util.Random

class Aurora(
    private val bulbGroup: BulbGroup,
    private val color: Color,
    /** In seconds */
    private val time: Int,
    /** In frames */
    private val fade: Int,
    /** Number of bulbs that possibly change between blocks */
    private val nChangeBulbs: Int,
    private val minBrightness: Float,
    private val brightnessScale: Float = 1.0f
) : EffectBase() {
    private val rnd: Random = Random(1337)
	private var alt_color: Color = Color(0,0,0)
	private var alt_amount: Float = 0.0f

    override suspend fun run() {
        val nRepeats = time * 20 / fade

        for (j in 0 until nRepeats) {
            for (i in 0 until nChangeBulbs) {
                val hsbValues = Color.RGBtoHSB(Math.round(color.red*(1.0f-alt_amount) + alt_color.red*alt_amount), Math.round(color.green*(1.0f-alt_amount) + alt_color.green*alt_amount), Math.round(color.blue*(1.0f-alt_amount) + alt_color.blue*alt_amount), null)
		val nextBulbIdx = rnd.nextInt(bulbGroup.numBulbs)
                val nextBulb = bulbGroup.getBulb(nextBulbIdx)
                var nextBrightness = rnd.nextFloat()
                while (nextBrightness <= minBrightness) {
                    nextBrightness = rnd.nextFloat()
                }
                val c = Color.getHSBColor(hsbValues[0], hsbValues[1], nextBrightness*brightnessScale)
                rgbFade(nextBulb, c, fade)
            }
            skip(fade)
        }
    }

	fun setAlternateColor(alternate: Color)
	{
		alt_color = alternate;
	}

	fun setAlternateColorAmount(amount: Float)
	{
		alt_amount = amount;
	}

    override fun toString() = "Effect Aurora"
}
