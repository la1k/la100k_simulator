package darkness.generator.api.effects

import darkness.generator.api.BulbRGB
import java.awt.Color
import kotlin.math.abs
import kotlin.math.atan2

/**
 * Fan scrolling effect
 */
class FanScroll(
    bulbs: Collection<BulbRGB>,
    period: Int,
    private var fanColor: Color,
    private var alternate: Boolean = false,
    private var rightToLeft: Boolean = false,
    scaling: Double = 1.0
) : EffectBase() {
    private var bulbs: List<BulbRGB> = bulbs.toList()
    private var centerX = 0f
    private var centerY = 0f
    private var farLeftAngle = 100.0
    private var farRightAngle = -100.0

    private var epsilon = scaling // TODO: Angle epsilon
    private var anglePerFrame = 0.0

    init {
        var minX = 100f
        var maxX = -100f

        // TODO: There is probably a smarter way to do this
        for (bulb in bulbs) {

            val posX = bulb.position[0]
            val posY = bulb.position[1]

            if (posX < minX)
                minX = posX
            else if (posX > maxX)
                maxX = posX

            if (posY < centerY)
                centerY = posY

        }

        this.centerX = (minX + maxX) / 2
        this.centerY -= 3f // Seems to look alright

        for (bulb in bulbs) {
            val posX = bulb.position[0]
            val posY = bulb.position[1]

            val angle = Math.atan2((posX - centerX).toDouble(), (posY - centerY).toDouble())

            if (angle < farLeftAngle) {
                farLeftAngle = angle
            } else if (angle > farRightAngle) {
                farRightAngle = angle
            }
        }

        anglePerFrame = (farRightAngle - farLeftAngle) / (period - 8)
        farLeftAngle -= 4 * anglePerFrame
        farRightAngle += 4 * anglePerFrame
        epsilon *= anglePerFrame * 3

        if (rightToLeft) {
            anglePerFrame *= -1.0
        }
    }

    override suspend fun run() {
        var state = if (rightToLeft) farRightAngle else farLeftAngle

        while (!isCancelled) {
            state += anglePerFrame

            for (bulb in bulbs) {
                val angle = atan2((bulb.position[0] - centerX).toDouble(), (bulb.position[1] - centerY).toDouble())
                if (abs(angle - state) < epsilon) {
                    set(bulb, fanColor)
                } else {
                    relinquish(bulb)
                }
            }

            if (alternate) {
                if (
                    state >= farRightAngle && anglePerFrame >= 0 ||
                    state <= farLeftAngle && anglePerFrame <= 0
                ) {
                    anglePerFrame *= -1.0
                }
            } else {
                if (rightToLeft) {
                    if (state <= farLeftAngle) {
                        state = farRightAngle
                    }
                } else {
                    if (state >= farRightAngle) {
                        state = farLeftAngle
                    }
                }
            }

            next()
        }

    }

    override fun toString() = "FanScroll running on (some bulb data here)"
}
