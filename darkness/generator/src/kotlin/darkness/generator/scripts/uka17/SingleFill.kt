package darkness.generator.scripts.uka17

import darkness.generator.api.BulbRGB

class SingleFill : BaseScript() {
    override suspend fun run() {
        super.run()
        var previousBulb: BulbRGB? = null
        var lastLetter = letters.last()
        var lastBulb: BulbRGB? = lastLetter.getBulb(letters.last().numBulbs - 1)

        do {
            for (letter in letters) {
                for (bulb in letter) {
                    set(bulb, 255, 255, 255)
                    if (bulb == lastBulb) {
                        lastBulb = previousBulb
                        skip(2)
                        lastLetter = letter
                        break
                    } else {
                        if (previousBulb != null) {
                            set(previousBulb, 0, 0, 0)
                        }
                        previousBulb = bulb
                        skip(1)
                    }
                }
                if (lastLetter == letter) {
                    break
                }
            }
        } while (letters[0].getBulb(0) !== lastBulb)

        skip(20)
    }
}
