package darkness.generator.scripts.la100k

import darkness.generator.api.BulbGroup
import darkness.generator.api.BulbRGB
import darkness.generator.api.ScriptBase
import java.awt.Color

open class BaseScript : ScriptBase() {
    protected lateinit var A: BulbGroup
    protected lateinit var B: BulbGroup
    protected lateinit var C: BulbGroup
    protected lateinit var D: BulbGroup
    protected lateinit var E: BulbGroup
    protected lateinit var F: BulbGroup

    protected lateinit var letters: List<BulbGroup>
    protected lateinit var allBulbs: List<BulbRGB>
    protected lateinit var allBulbsGroup: BulbGroup
    protected lateinit var uke_lilla: Color
    protected lateinit var uke_gull: Color
    protected lateinit var uke_blå: Color
    protected lateinit var uke_turkis: Color
    protected lateinit var uke_rød: Color

    override suspend fun run() {
        A = group(31, 32, 33, 34, 35)
        B = group(1, 36, 37, 38, 39, 40, 41, 42)
        C = group(27, 28, 29, 30)
        D = group(19, 20, 21, 22, 23, 24, 25, 26)
        E = group(3, 4, 5, 14, 15, 16, 17, 18)
        F = group(2, 6, 7, 8, 9, 10, 11, 12, 13)

        letters = listOf(A, B, C, D, E, F)
        uke_lilla = Color(102, 0, 152)
        uke_gull = Color(255, 205, 0)
        uke_blå = Color(0, 0, 255)
        uke_turkis = Color(172, 246, 231)
        uke_rød = Color(229, 1, 1)

        allBulbs = letters.flatMap { letter -> letter.allBulbs }
        allBulbsGroup = BulbGroup(allBulbs)

    }
}
