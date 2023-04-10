package darkness.generator.scripts.uka15

class RGBTest : BaseScript() {
    override suspend fun run() {
        super.run()

        for (letter in letters) {
            set(letter, 255, 0, 0)
            skip(40)
            set(letter, 0, 255, 0)
            skip(40)
            set(letter, 0, 0, 255)
            skip(40)
            set(letter, 0, 0, 0)
        }
        for (column in columns) {
            for (bulb in column) {
                set(bulb, 255, 0, 0)
                skip(5)
                set(bulb, 0, 255, 0)
                skip(5)
                set(bulb, 0, 0, 255)
                skip(5)
                set(bulb, 0, 0, 0)

            }
        }
    }
}
