package darkness.generator.scripts.uka15

class SimenIntro : BaseScript() {
    override suspend fun run() {
        super.run()

        // start with white
        for (letter in letters) {
            set(letter, 20, 20, 20)
        }
        skip(40)

        // start glowing
        for (letter in letters) {
            rgbFade(letter, 100, 100, 100, 40)
        }
        skip(40)

        // increase I
        for (letter in letters) {
            rgbFade(letter, 20, 20, 20, 40)
        }
        rgbFade(letters[3], 255, 255, 255, 40)
        skip(60)

        // flow copper
        val centerColunt = columns.size / 2 - 4

        //rgbFade((BulbGroup)columns[centerColunt],20,20,20,8);

        hsbFade(letters[3], floatArrayOf(27.0f / 360, 1f, 0.5f), 24)

        for (i in 1 until columns.size / 2 - 3) {
            hsbFade(columns[centerColunt - i], floatArrayOf(27.0f / 360, 1f, 0.5f), 8)
            hsbFade(columns[centerColunt + i], floatArrayOf(27.0f / 360, 1f, 0.5f), 8)
            skip(4)
        }


        for (i in columns.size / 2 - 3 until columns.size / 2 + 5) {

            hsbFade(columns[centerColunt + i], floatArrayOf(27.0f / 360, 1f, 0.5f), 8)
            skip(4)
        }

        skip(60)


        for (letter in letters) {
            hsbFade(letter, floatArrayOf(27.0f / 360, 1f, 1f), 15)
            skip(20)
            hsbFade(letter, floatArrayOf(27.0f / 360, 1f, 0.5f), 10)
            skip(10)

        }

        skip(20)
        for (letter in letters) {
            hsbFade(letter, floatArrayOf(27.0f / 360, 0f, 1f), 40)

        }
        skip(80)
    }
}
