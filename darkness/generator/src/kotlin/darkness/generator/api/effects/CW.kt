package darkness.generator.api.effects

import darkness.generator.api.BulbSet

import java.awt.*
import java.util.ArrayList
import java.util.HashMap

class CW @JvmOverloads constructor(private val bulbSet: BulbSet, str: String, private val color: Color = Color.LIGHT_GRAY, private val framesPerDit: Int = 3) : EffectBase() {
    private val str = str.toLowerCase()

    var totalFrames: Int = 0
        private set
    private var sequence = ArrayList<ArrayList<Array<Boolean>>>()

    init {
        val words = str.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

        // Build an array of arrays (word) of arrays(letter in morse) of bools (dits and dahs)
        for (word in words) {
            val morseWord = ArrayList<Array<Boolean>>()

            for (c in word.toCharArray()) {
                morseWord.add(morseCode[c]!!)
            }

            sequence.add(morseWord)
        }

        // Really crude computation, could be more effective, but since this runs offline, meh...
        for (word in sequence) {
            for (c in word) {
                for (ditdah in c) {
                    // One for dit, 3 for dah, + one for separation
                    totalFrames += (if (ditdah) framesPerDit else 3 * framesPerDit) + framesPerDit
                }

                // Add another 2 for character separation
                totalFrames += 2 * framesPerDit
            }

            // Add another 4 for word separation
            totalFrames += 4 * framesPerDit

        }
    }

    override suspend fun run() {
        // Pump out morse
        //
        // Dit = one period
        // Dah = threee periods
        // Space equal to one dit between dits and dahs
        // Space equal to one dah between characters
        // Space equal to seven dits between words

        val oldColor = bulbSet.color

        for (word in sequence) {
            for (c in word) {
                if (isCancelled)
                    return

                for (ditdah in c) {
                    set(bulbSet, color)
                    skip(if (ditdah) framesPerDit else 3 * framesPerDit) // Skip 3xdit for a dah
                    set(bulbSet, oldColor)
                    skip(framesPerDit)
                }

                // Skip two more dits between characters
                skip(2 * framesPerDit)
            }

            // Skip 4 more dits to total 7 between words
            skip(4 * framesPerDit)
        }

    }

    override fun toString() = "Effect CW on $bulbSet for color $color over $totalFrames frames."

    companion object {
        /**
         * Map of CW codes for characters.
         * True = dit
         * False = dah
         */
        private val morseCode = HashMap<Char, Array<Boolean>>()

        init {
            morseCode['a'] = arrayOf(true, false)
            morseCode['b'] = arrayOf(false, true, true, true)
            morseCode['c'] = arrayOf(false, true, false, true)
            morseCode['d'] = arrayOf(false, true, true)
            morseCode['e'] = arrayOf(true)
            morseCode['f'] = arrayOf(true, true, false, true)
            morseCode['g'] = arrayOf(false, false, true)
            morseCode['h'] = arrayOf(true, true, true, true)
            morseCode['i'] = arrayOf(true, true)
            morseCode['j'] = arrayOf(true, false, false, false)
            morseCode['k'] = arrayOf(false, true, false)
            morseCode['l'] = arrayOf(true, false, true, true)
            morseCode['m'] = arrayOf(false, false)
            morseCode['n'] = arrayOf(false, true)
            morseCode['o'] = arrayOf(false, false, false)
            morseCode['p'] = arrayOf(true, false, false, true)
            morseCode['q'] = arrayOf(false, false, true, false)
            morseCode['r'] = arrayOf(true, false, true)
            morseCode['s'] = arrayOf(true, true, true)
            morseCode['t'] = arrayOf(false)
            morseCode['u'] = arrayOf(true, true, false)
            morseCode['v'] = arrayOf(true, true, true, false)
            morseCode['w'] = arrayOf(true, false, false)
            morseCode['x'] = arrayOf(false, true, true, false)
            morseCode['y'] = arrayOf(false, true, false, false)
            morseCode['z'] = arrayOf(false, false, true, true)

            morseCode['0'] = arrayOf(false, false, false, false, false)
            morseCode['1'] = arrayOf(true, false, false, false, false)
            morseCode['2'] = arrayOf(true, true, false, false, false)
            morseCode['3'] = arrayOf(true, true, true, false, false)
            morseCode['4'] = arrayOf(true, true, true, true, false)
            morseCode['5'] = arrayOf(true, true, true, true, true)
            morseCode['6'] = arrayOf(false, true, true, true, true)
            morseCode['7'] = arrayOf(false, false, true, true, true)
            morseCode['8'] = arrayOf(false, false, false, true, true)
            morseCode['9'] = arrayOf(false, false, false, false, true)

            morseCode['.'] = arrayOf(true, false, true, false, true, false)
            morseCode[','] = arrayOf(false, false, true, true, false, false)
            morseCode['?'] = arrayOf(true, true, false, false, true, true)
            morseCode['\''] = arrayOf(true, false, false, false, false, true)
            morseCode['!'] = arrayOf(false, true, false, true, false, false)
            morseCode['/'] = arrayOf(false, true, true, false, true)
            morseCode['('] = arrayOf(false, true, false, false, true)
            morseCode[')'] = arrayOf(false, true, false, false, true, false)
            morseCode['&'] = arrayOf(true, false, true, true, true)
            morseCode[':'] = arrayOf(false, false, false, true, true, true)
            morseCode[';'] = arrayOf(false, true, false, true, false, true)
            morseCode['='] = arrayOf(false, true, true, true, false)
            morseCode['+'] = arrayOf(true, false, true, false, true)
            morseCode['-'] = arrayOf(false, true, true, true, true, false)
            morseCode['_'] = arrayOf(true, true, false, false, true, false)
            morseCode['"'] = arrayOf(true, false, true, true, false, true)
            morseCode['$'] = arrayOf(true, true, true, false, true, true, false)
            morseCode['@'] = arrayOf(true, false, false, true, false, true)
        }
    }
}
