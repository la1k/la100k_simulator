package darkness.generator.scripts.uka15

import darkness.generator.api.BulbGroup

class Pong : BaseScript() {
    override suspend fun run() {
        super.run()

        for (letter in letters) {
            rgbFade(letter, 0, 0, 0, 20)
        }
        skip(20)

        val lPadSpace = group(0, 1, 2, 3, 4, 5, 6)
        val rPadSpace = group(87, 86, 85, 84, 83, 92, 93, 94, 95)

        var lPad = BulbGroup(lPadSpace.getBulb(2), lPadSpace.getBulb(3))

        rgbFade(lPad, 255, 0, 0, 20)
        skip(40)
        //mov left pad up
        for (i in 1..2) {
            lPad = BulbGroup(lPadSpace.getBulb(2 - i), lPadSpace.getBulb(3 - i))
            set(lPadSpace, 0, 0, 0)
            set(lPad, 255, 0, 0)
            skip(10)
        }

        //mov left pad down
        for (i in 1..4) {
            lPad = BulbGroup(lPadSpace.getBulb(0 + i), lPadSpace.getBulb(1 + i))
            set(lPadSpace, 0, 0, 0)
            set(lPad, 255, 0, 0)
            skip(10)
        }
        //mov left pad to center

        for (i in 1..4) {
            lPad = BulbGroup(lPadSpace.getBulb(6 - i), lPadSpace.getBulb(7 - i))
            set(lPadSpace, 0, 0, 0)
            set(lPad, 255, 0, 0)
            skip(10)
        }
        skip(10)
        set(lPad, 255, 255, 255)
        skip(5)
        set(lPad, 255, 0, 0)
        skip(5)
        set(lPad, 255, 255, 255)
        skip(5)
        set(lPad, 255, 0, 0)
        skip(20)


        var rPad = BulbGroup(rPadSpace.getBulb(3), rPadSpace.getBulb(4))

        rgbFade(rPad, 0, 0, 255, 20)
        skip(40)
        //mov left pad up
        for (i in 1..3) {
            rPad = BulbGroup(rPadSpace.getBulb(3 - i), rPadSpace.getBulb(4 - i))
            set(rPadSpace, 0, 0, 0)
            set(rPad, 0, 0, 255)
            skip(10)
        }

        //mov left pad down
        for (i in 1..7) {
            rPad = BulbGroup(rPadSpace.getBulb(0 + i), rPadSpace.getBulb(1 + i))
            set(rPadSpace, 0, 0, 0)
            set(rPad, 0, 0, 255)
            skip(10)
        }
        //mov left pad to center

        for (i in 1..4) {
            rPad = BulbGroup(rPadSpace.getBulb(7 - i), rPadSpace.getBulb(8 - i))
            set(rPadSpace, 0, 0, 0)
            set(rPad, 0, 0, 255)
            skip(10)
        }
        skip(10)
        set(rPad, 255, 255, 255)
        skip(5)
        set(rPad, 0, 0, 255)
        skip(5)
        set(rPad, 255, 255, 255)
        skip(5)
        set(rPad, 0, 0, 255)
        skip(20)


        // ball appers
        set(bulb(47), 255, 255, 0)
        skip(5)
        set(bulb(47), 0, 0, 0)

        set(bulb(52), 255, 255, 0)
        skip(5)
        set(bulb(52), 0, 0, 0)

        set(bulb(67), 255, 255, 0)
        skip(5)
        set(bulb(67), 0, 0, 0)

        set(bulb(75), 255, 255, 0)
        skip(5)
        set(bulb(75), 0, 0, 0)

        set(bulb(71), 255, 255, 0)
        skip(5)
        set(bulb(71), 0, 0, 0)

        set(bulb(91), 255, 255, 0)
        skip(5)
        set(bulb(91), 0, 0, 0)

        set(bulb(69), 255, 255, 0)
        skip(5)
        set(bulb(69), 0, 0, 0)

        lPad = BulbGroup(lPadSpace.getBulb(1), lPadSpace.getBulb(2))
        set(lPadSpace, 0, 0, 0)
        set(lPad, 255, 0, 0)


        set(bulb(56), 255, 255, 0)
        skip(5)
        set(bulb(56), 0, 0, 0)

        set(bulb(53), 255, 255, 0)
        skip(5)
        set(bulb(53), 0, 0, 0)

        lPad = BulbGroup(lPadSpace.getBulb(2), lPadSpace.getBulb(3))
        set(lPadSpace, 0, 0, 0)
        set(lPad, 255, 0, 0)

        set(bulb(47), 255, 255, 0)
        skip(5)
        set(bulb(47), 0, 0, 0)

        set(bulb(41), 255, 255, 0)
        skip(5)
        set(bulb(41), 0, 0, 0)

        lPad = BulbGroup(lPadSpace.getBulb(3), lPadSpace.getBulb(4))
        set(lPadSpace, 0, 0, 0)
        set(lPad, 255, 0, 0)

        set(bulb(26), 255, 255, 0)
        skip(5)
        set(bulb(26), 0, 0, 0)

        set(bulb(20), 255, 255, 0)
        skip(5)
        set(bulb(20), 0, 0, 0)

        lPad = BulbGroup(lPadSpace.getBulb(4), lPadSpace.getBulb(5))
        set(lPadSpace, 0, 0, 0)
        set(lPad, 255, 0, 0)

        set(bulb(12), 255, 255, 0)
        skip(5)
        set(bulb(12), 0, 0, 0)

        set(bulb(1), 255, 255, 0)
        skip(5)


        lPad = BulbGroup(lPadSpace.getBulb(3), lPadSpace.getBulb(4))
        set(lPadSpace.getBulb(5), 0, 0, 0)
        set(lPad, 255, 0, 0)
        skip(40)

        for (letter in letters) {
            set(letter, 255, 255, 255)
        }
        skip(10)
        for (letter in letters) {
            set(letter, 0, 0, 0)
        }
        skip(10)
        for (letter in letters) {
            set(letter, 255, 255, 255)
        }
        skip(10)
        for (letter in letters) {
            set(letter, 0, 0, 0)
        }
        skip(10)
        for (letter in letters) {
            set(letter, 255, 255, 255)
        }
        skip(10)
        for (letter in letters) {
            set(letter, 0, 0, 0)
        }
        skip(10)
    }
}
