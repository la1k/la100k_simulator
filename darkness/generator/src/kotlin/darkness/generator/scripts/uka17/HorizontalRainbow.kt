package darkness.generator.scripts.uka17

class HorizontalRainbow : BaseScript() {
    private val rainbow48 = intArrayOf(255, 255, 255, 255, 255, 255, 255, 255, 255, 223, 191, 159, 128, 96, 64, 32, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 32, 64, 96, 128, 159, 191, 223, 255, 255, 255, 255, 255, 255, 255, 255)

    override suspend fun run() {
        super.run()
        scrollrainbow(5)
    }

    private suspend fun scrollrainbow(loops: Int) {
        for (k in 0 until loops) {
            for (i in rainbow48.indices) {
                for (j in 0 until columns.size) {
                    set(columns[j], rainbow48[(i + j) % rainbow48.size], rainbow48[(i + j + 16) % rainbow48.size], rainbow48[(i + j + 32) % rainbow48.size])
                }

                skip(1)
            }
        }
    }
}
