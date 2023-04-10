package darkness.generator.scripts.uka17

import java.awt.Color

class StartupFrode : BaseScript() {
    internal var timeoutfade = IntArray(150)

    override suspend fun run() {
        super.run()

        for (i in timeoutfade.indices)
            timeoutfade[i] = 0

        allon(Color(218, 165, 32), 60)
        for (i in 0..19) {
            for (j in 10 downTo 6)
                sparkleFade(6, 500, Color(218, 165, 32), Color(64, 10, 32), Color(218, 165, 32), 0, j * 10, 100 - j * 10)
            for (j in 5..9)
                sparkleFade(6, 500, Color(218, 165, 32), Color(64, 10, 32), Color(218, 165, 32), 0, j * 10, 100 - j * 10)
        }
        for (i in 0..99)
            sparkleFade(2, 500, Color(218, 165, 32), Color(32, 10, 16), Color(218, 165, 32), i, 60, 40)
        sparkleFade(60 * 20, 500, Color(218, 165, 32), Color(32, 10, 16), Color(218, 155, 32), 100, 50, 50)
    }

    private suspend fun allon(c: Color, time: Int) {
        for (bulb in allBulbs)
            set(bulb, c)
        skip(20 * time)
    }

    private suspend fun sparkleFade(time: Int, probability: Int, mid: Color, `var`: Color, feat: Color, fade: Int, intensmid: Int, intensvar: Int) {
        for (i in 0 until time) {
            for (letter in letters)
                for (bulb in letter) {
                    val `val` = Math.random()
                    if (randint(1000) < probability) {
                        var intens = randint(intensvar * 2) - intensvar + intensmid
                        if (intens > 100)
                            intens = 100
                        val randR = randint(`var`.red * 2) - `var`.red
                        val randG = randint(`var`.green * 2) - `var`.green
                        val randB = randint(`var`.blue * 2) - `var`.blue
                        var valR = (mid.red + randR) * (100 - fade) / 100 * intens / 100
                        var valG = (mid.green + randG) * (100 - fade) / 100 * intens / 100
                        var valB = (mid.blue + randB) * (100 - fade) / 100 * intens / 100

                        for (digit in digits)
                            for (dbulb in digit)
                                if (bulb == dbulb) {
                                    valR = valR + feat.red * fade / 100
                                    valG = valG + feat.green * fade / 100
                                    valB = valB + feat.blue * fade / 100
                                }
                        if (valR > 255)
                            valR = 255
                        if (valG > 255)
                            valG = 255
                        if (valB > 255)
                            valB = 255

                        val c = Color(valR, valG, valB)

                        if (timeoutfade[bulb.id] == 0) {
                            rgbFade(bulb, c, 15)
                            timeoutfade[bulb.id] = 15
                        }
                    }
                }
            skip(1)
            for (j in timeoutfade.indices)
                if (timeoutfade[j] != 0)
                    timeoutfade[j]--
        }
    }

    private fun randint(value: Int): Int {
        return (Math.random() * value).toInt()
    }
}
