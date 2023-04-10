package darkness.generator.scripts.uka17

class CountDown : BaseScript() {
    override suspend fun run() {
        super.run()
        for (i in 5 downTo 2) {
            setCounter(i, true)
            skip(60 * 20)
        }
        for (i in 60 downTo 0) {
            setCounter(i, false)
            skip(20)
        }
    }
}
