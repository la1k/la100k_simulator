package darkness.generator.scripts.uka21

class Rainbow : BaseScript() {
    override suspend fun run() {
        super.run()
        for (bulb in allBulbs) {
            set(bulb, 255, 0, 0)
            
            skip(2)
        }
        for (bulb in letters) {
            set(bulb, 0, 255, 0)
            skip(2)
        }
        for (bulb in letters) {
            set(bulb, 0, 0, 255)
            skip(2)
        }

    }
}
