package darkness.generator.scripts.uka21

class Countdown : BaseScript() {
    override suspend fun run() {
        super.run()

        for (seconds in 5 * 60 downTo 1) {
            val ones = seconds % 10
            val tens = seconds / 10

            set(allBulbsGroup, 0, 0, 0)
            for (i in 0 until ones) {
                set(allBulbs[i], 127, 127, 127)
            }
            for (i in 0 until tens) {
                set(allBulbs[A.numBulbs + i], 127, 127, 127)
            }

            skip(20)
        }
    }
}
