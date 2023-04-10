package darkness.generator.scripts.uka13

import darkness.generator.api.BulbGroup
import darkness.generator.api.BulbRGB
import darkness.generator.api.ScriptBase

abstract class DemoBaseScript : ScriptBase() {
    protected lateinit var topOfJ: BulbRGB
    protected lateinit var J: BulbGroup
    protected lateinit var U: BulbGroup
    protected lateinit var B: BulbGroup
    protected lateinit var A: BulbGroup
    protected lateinit var L: BulbGroup
    protected lateinit var O: BulbGroup
    protected lateinit var N: BulbGroup
    protected lateinit var G: BulbGroup
    protected lateinit var letters: List<BulbGroup>

    override suspend fun run() {
        topOfJ = bulb(6)
        J = group(0, 1, 2, 3, 4, 5, 6)
        U = group(10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20)
        B = group(24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39)
        A = group(42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54)
        L = group(58, 59, 60, 61, 62, 63, 64, 65)
        O = group(138, 139, 140, 107, 106, 105, 104, 108, 123, 122, 121, 103, 109,
                124, 133, 132, 120, 102, 110, 125, 134, 137, 131, 119, 101, 111, 126,
                135, 136, 130, 118, 112, 127, 128, 129, 117, 113, 114, 115, 116)
        N = group(70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83)
        G = group(87, 88, 89, 90, 91, 92, 93, 94, 95, 96, 97)
        letters = listOf(J, U, B, A, L, O, N, G)
    }
}
