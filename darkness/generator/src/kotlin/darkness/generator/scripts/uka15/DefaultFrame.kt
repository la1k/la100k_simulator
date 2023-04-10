package darkness.generator.scripts.uka15

class DefaultFrame : BaseScript() {
    override suspend fun run() {
        super.run()

        val fadeInOrder = listOf(C, F, A, D, G, E, B)

        for (idx in fadeInOrder.indices) {
            val letter = fadeInOrder[idx]
            // Get a color for our letter
            val colorAngle = idx.toFloat() / fadeInOrder.size.toFloat()
            setHSB(letter, colorAngle, 1.0f, 0.8f)
        }

        next()
    }
}
