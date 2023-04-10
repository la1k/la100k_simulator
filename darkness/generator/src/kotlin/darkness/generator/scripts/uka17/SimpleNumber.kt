package darkness.generator.scripts.uka17

class SimpleNumber : BaseScript() {
    override suspend fun run() {
        super.run()
        for (digit in digits) {
            set(digit, 255, 255, 255)
        }
    }
}
