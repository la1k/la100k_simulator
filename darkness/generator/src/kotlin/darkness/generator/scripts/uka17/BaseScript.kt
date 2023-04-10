package darkness.generator.scripts.uka17

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
    protected lateinit var H: BulbGroup
    protected lateinit var I: BulbGroup
    protected lateinit var letters: List<BulbGroup>
    protected lateinit var digitA: BulbGroup
    protected lateinit var digitB: BulbGroup
    protected lateinit var digitC: BulbGroup
    protected lateinit var digits: List<BulbGroup>
    protected lateinit var columns: List<BulbGroup>
    protected lateinit var allBulbs: List<BulbGroup>

    protected lateinit var allBulbsRGB: List<BulbRGB> // ...except for the counter

    protected lateinit var mergedAllBulbs: BulbGroup // happy with one group containing all bulbs ;)

    protected lateinit var counter: List<BulbGroup>

    override suspend fun run() {
        A = group(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11)
        B = group(15, 16, 17, 18, 19, 20, 21)
        C = group(25, 26)
        D = group(30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43)
        E = group(45, 46, 47, 48, 49, 50, 51, 52, 53, 54)
        F = group(55, 56)
        G = group(60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73)
        H = group(75, 76, 77, 78, 79, 80, 81, 82, 83, 84)
        I = group(85, 86, 87, 88, 89, 90, 91, 92, 93, 94, 95)
        letters = listOf(A, B, C, D, E, F, G, H, I)

        digitA = group(0, 1, 2, 3, 4, 5, 6)
        digitB = group(30, 31, 32, 33, 38, 39, 40, 41, 42, 43)
        digitC = group(60, 61, 62, 63, 68, 69, 70, 71, 72, 73)
        digits = listOf(digitA, digitB, digitC)

        allBulbs = listOf(group(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11), group(15, 16, 17, 18, 19, 20, 21), group(25, 26), group(30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43), group(45, 46, 47, 48, 49, 50, 51, 52, 53, 54), group(55, 56), group(60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73), group(75, 76, 77, 78, 79, 80, 81, 82, 83, 84), group(85, 86, 87, 88, 89, 90, 91, 92, 93, 94, 95))

        columns = listOf(group(6), group(0, 2, 4), group(1, 3, 5), group(7, 8, 9, 10), group(11, 15), group(17, 18), group(16, 21), group(20), group(19), group(25, 26), group(30, 31, 32, 33), group(34, 35, 36, 37, 38, 43), group(39, 42), group(40, 41), group(45, 46, 47, 48), group(50, 51, 52, 53), group(49, 54), group(55, 56), group(60, 61, 62, 63), group(64, 65, 66, 67, 68, 73), group(69, 72), group(70, 71), group(75, 76, 77), group(82, 83, 84), group(78), group(79, 80, 81), group(85, 86, 87, 88, 89), group(95), group(90, 91, 92, 93, 94))

        allBulbsRGB = letters.flatMap(BulbGroup::allBulbs)

        mergedAllBulbs = BulbGroup(allBulbsRGB)

        counter = listOf(group(101, 102, 103, 104, 105, 106, 107), group(108, 109, 110, 111, 112, 113, 114))
    }

    protected fun mergeBulbGroups(groups: List<BulbGroup>): BulbGroup {
        return BulbGroup(groups.flatMap(BulbGroup::allBulbs))
    }

    protected fun mergeBulbGroups(vararg groups: BulbGroup) = mergeBulbGroups(groups.toList())

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
        protected val counterDigits = arrayOf(intArrayOf(0, 1, 2, 3, 4, 5), // 0
                intArrayOf(1, 2), // 1
                intArrayOf(0, 1, 6, 4, 3), // 2
                intArrayOf(0, 1, 6, 2, 3), // 3
                intArrayOf(5, 6, 1, 2), // 4
                intArrayOf(0, 5, 6, 2, 3), // 5
                intArrayOf(0, 5, 4, 3, 2, 6), // 6
                intArrayOf(0, 1, 2), // 7
                intArrayOf(0, 1, 2, 3, 4, 5, 6), // 8
                intArrayOf(6, 5, 0, 1, 2, 3))// 9

        protected val LETTER_HEIGHT = 2.05
        protected val LEFT = 0.14
        protected val RIGHT = 6.33
        protected val WIDTH = RIGHT - LEFT
    }
}
