package darkness.generator.scripts.uka17

class Default : BaseScript() {
    override suspend fun run() {
        super.run()
        yellow()
    }

    private fun yellow() {
        for (letters in allBulbs) {
            set(letters, 218, 165, 32)
        }

        for (digits in digits) {
            set(digits, 255, 83, 16)
        }
        set(I, 255, 83, 16)
    }
}
