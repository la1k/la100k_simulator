package darkness.generator.scripts.uka17

class YellowBang : BaseScript() {
    internal var nScroll = 4 // How many times should the scroll scroll
    internal var timeFrames = 4 //How long should the last character blink
    override suspend fun run() {
        super.run()
        scrollIn(nScroll)
        bang(timeFrames)
        skip(4)
    }

    private suspend fun scrollIn(nScroll: Int) {
        for (j in 0 until nScroll) {

            skip(10)
            for (letters in allBulbs) {
                set(letters, 0, 0, 0)
            }
            for (i in 0 until columns.size - 3) {
                val column = columns[i]
                set(column, 0, 128, 128)
                skip(1)
            }
        }
        skip(10)
    }

    private suspend fun bang(timeFrames: Int) {
        for (i in 0 until timeFrames) {
            set(I, 0, 128, 128)
            skip(3)
            set(I, 0, 0, 0)
            skip(3)
            set(I, 0, 128, 128)
            skip(3)
        }
    }
}
