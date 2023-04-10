package darkness.generator.scripts.uka17

class HorizontalWave : BaseScript() {
    private val rainbow48 = intArrayOf(255, 255, 255, 255, 255, 255, 255, 255, 255, 223, 191, 159, 128, 96, 64, 32, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 32, 64, 96, 128, 159, 191, 223, 255, 255, 255, 255, 255, 255, 255, 255)

    private var phase = 0.0

    override suspend fun run() {
        super.run()
        waveover(5)
    }

    private suspend fun waveover(loops: Int) {
        for (k in 0 until loops * rainbow48.size) {
            for (j in 0 until columns.size) {
                val valR = (rainbow48[k % rainbow48.size] * (2 + Math.sin(phase + j.toDouble() * 2.0 * Math.PI / 40)) / 3.0).toInt()
                val valG = (rainbow48[(k + 6) % rainbow48.size] * (2 + Math.sin(phase + j.toDouble() * 2.0 * Math.PI / 40)) / 3.0).toInt()
                val valB = (rainbow48[(k + 12) % rainbow48.size] * (2 + Math.sin(phase + j.toDouble() * 2.0 * Math.PI / 40)) / 3.0).toInt()
                set(columns[j], valR, valG, valB)
            }
            skip(1)
            phase -= 2 * Math.PI / 40
        }
    }
}
