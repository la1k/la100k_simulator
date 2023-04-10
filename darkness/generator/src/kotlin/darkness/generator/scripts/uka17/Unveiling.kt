package darkness.generator.scripts.uka17

class Unveiling : BaseScript() {
    override suspend fun run() {
        super.run()
        merge(CountDown())
        skip(6000)
        merge(Genesis())
        skip(20)
        turnOffCounter()
    }
}
