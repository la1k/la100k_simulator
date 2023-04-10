package darkness.generator.scripts.uka19

import darkness.generator.api.BulbGroup
import darkness.generator.api.BulbRGB
import darkness.generator.api.ScriptBase

open class BaseScript : ScriptBase() {
    protected lateinit var A: BulbGroup
    protected lateinit var B: BulbGroup
    protected lateinit var C: BulbGroup
    protected lateinit var D: BulbGroup
    protected lateinit var E: BulbGroup
    protected lateinit var F: BulbGroup
    protected lateinit var G: BulbGroup
    protected lateinit var letters: List<BulbGroup>
    protected lateinit var allBulbs: List<BulbRGB>
    protected lateinit var allBulbsGroup: BulbGroup
    protected lateinit var accents: BulbGroup
    protected lateinit var counter: List<BulbGroup>

    override suspend fun run() {
        A = group(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)
        B = group(10, 11, 12, 13, 14)
        C = group(20, 21, 22, 23, 24, 25, 26, 27)
        D = group(30, 31, 32, 33, 34)
        E = group(40, 41, 42, 43, 44, 45)
        F = group(50, 51, 52, 53, 54)
        G = group(60, 61, 62, 63, 64, 65, 66, 67, 68, 69)
        letters = listOf(A, B, C, D, E, F, G)

        allBulbs = letters.flatMap { letter -> letter.allBulbs }
        allBulbsGroup = BulbGroup(allBulbs)

        accents = group(14, 34, 68, 69)
        counter = listOf(group(101, 102, 103, 104, 105, 106, 107), group(108, 109, 110, 111, 112, 113, 114))
    }

    protected fun setCounter(number: Int, leadingZero: Boolean) {
        setDigit(0, number / 10, leadingZero)
        setDigit(1, number % 10, true)
    }

    protected fun turnOffCounter() {
        for (digit in counter) {
            for (bulb in digit) {
                set(bulb, 0, 0, 0)
            }
        }
    }

    private fun setDigit(digitIndex: Int, digit: Int, showZero: Boolean) {
        val bulbs = counter[digitIndex].allBulbs
        val bulbIndices = counterDigits[digit]
        for (bulb in bulbs) {
            set(bulb, 0, 0, 0)
        }
        if (digit != 0 || showZero) {
            for (i in bulbIndices) {
                set(bulbs[i], 255, 0, 0)
            }
        }
    }

    companion object {
        private val counterDigits = listOf(
            listOf(0, 1, 2, 3, 4, 5),    // 0
            listOf(1, 2),                // 1
            listOf(0, 1, 6, 4, 3),       // 2
            listOf(0, 1, 6, 2, 3),       // 3
            listOf(5, 6, 1, 2),          // 4
            listOf(0, 5, 6, 2, 3),       // 5
            listOf(0, 5, 4, 3, 2, 6),    // 6
            listOf(0, 1, 2),             // 7
            listOf(0, 1, 2, 3, 4, 5, 6), // 8
            listOf(6, 5, 0, 1, 2, 3)     // 9
        )
    }
}
