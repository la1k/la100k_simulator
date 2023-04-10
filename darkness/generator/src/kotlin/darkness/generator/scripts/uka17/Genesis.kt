package darkness.generator.scripts.uka17

import darkness.generator.api.BulbGroup
import java.awt.Color
import java.util.*

/**
 * Opening sequence for Lysreklamen UKA 17.
 *
 * Runs a glittering effect on all bulbs for n seconds, before revealing the
 * 100th anniversary message.
 *
 * Uses a slightly modified version of the
 * [darkness.generator.api.effects.Aurora] effect.
 */
class Genesis : BaseScript() {
    private val RANDOM = Random(1337)
    private val GOLDENROD = Color(218, 165, 32)

    override suspend fun run() {
        super.run()

        val everything = mergeBulbGroups(letters)
        val onlyDigits = mergeBulbGroups(digits)
        val nonDigits = mergeBulbGroups(
            B, C, E, F, H, I,
            group(7, 8, 9, 10, 11),
            group(34, 35, 36, 37),
            group(64, 65, 66, 67)
        )

        val FRAMES_PER_SECOND = 20
        val glitterFrames = FRAMES_PER_SECOND * 20 // TODO change to actual frame number
        val fadeFrames = FRAMES_PER_SECOND * 5

        glitter(everything, GOLDENROD, glitterFrames, 0.1f, 0.8f)

        // Make sure that each bulb fade from its current color
        for (bulb in nonDigits) {
            rgbFade(bulb, Color.BLACK, fadeFrames)
        }
        glitter(onlyDigits, GOLDENROD, glitterFrames / 2, 0.1f, 0.8f)

        // Make sure that each bulb fade from its current color
        for (bulb in onlyDigits) {
            rgbFade(bulb, GOLDENROD, fadeFrames / 2)
        }
        skip(fadeFrames)
        glitter(onlyDigits, GOLDENROD, glitterFrames / 2, 0.2f, 1.0f)
    }

    private fun randomFloat(left: Float, right: Float): Float {
        return left + RANDOM.nextFloat() * (right - left)
    }

    private suspend fun glitter(bulbGroups: BulbGroup, color: Color, frames: Int, min: Float, max: Float) {
        glitter(bulbGroups, color, frames, 4, bulbGroups.numBulbs / 2, min, max)
    }

    /**
     * Make a group of bulbs glitter.
     *
     * @param bulbGroup Group of bulbs to make glittering.
     * @param color Glittering base color.
     * @param frames Glitter duration in frames.
     * @param fade Fade time in frames.
     * @param glitteringBulbs Number of bulbs that change brightness during glittering.
     * @param min Minimum brightness.
     * @param max Maximum brightness.
     */
    private suspend fun glitter(bulbGroup: BulbGroup, color: Color, frames: Int, fade: Int, glitteringBulbs: Int, min: Float, max: Float) {
        var loops = frames / fade
        val hsbValues = FloatArray(3)
        Color.RGBtoHSB(color.red, color.green, color.blue, hsbValues)

        while (loops > 0) {
            for (i in 0 until glitteringBulbs) {
                val index = RANDOM.nextInt(bulbGroup.numBulbs)
                val bulb = bulbGroup.getBulb(index)
                val brightness = randomFloat(min, max)
                hsbFade(bulb, hsbValues[0], hsbValues[1], brightness, fade)
            }
            skip(fade)
            loops--
        }
    }
}
