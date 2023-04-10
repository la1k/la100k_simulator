package darkness.generator.scripts.uka21

import darkness.generator.api.BulbGroup
import java.awt.Color

class UKAColor : BaseScript() {
    override suspend fun run() {
        super.run()

        var uke_lilla = Color(102, 0, 152)
        var uke_gull = Color(255, 205, 0)
        var uke_blå = Color(0,0,255)
        var uke_turkis = Color(172, 246, 231)
        var uke_rød = Color(229, 1, 1)

        for (letter in letters) {
            set(letter, uke_lilla)
        }
        skip(50)
        for (letter in letters) {
            set(letter, uke_gull)
        }
        skip(50)
        for (letter in letters) {
            set(letter, uke_blå)
        }
        skip(50)
        for (letter in letters) {
            set(letter, uke_turkis)
        }
        skip(50)
        for (letter in letters) {
            set(letter, uke_rød)
        }
        skip(50)
    }
}
