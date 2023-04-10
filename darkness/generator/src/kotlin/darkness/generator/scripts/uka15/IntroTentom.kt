package darkness.generator.scripts.uka15

import darkness.generator.api.BulbSet
import darkness.generator.api.effects.EffectBase
import java.util.*

class IntroTentom : BaseScript() {
    override suspend fun run() {
        super.run()

        // phase1: neon sign start up
        // Phase2: fade to red

        val LetterA = BlinkyLetter()
        LetterA.setLetter(A)
        val LetterB = BlinkyLetter()
        LetterB.setLetter(B)
        val LetterC = BlinkyLetter()
        LetterC.setLetter(C)
        val LetterD = BlinkyLetter()
        LetterD.setLetter(D)
        val LetterE = BlinkyLetter()
        LetterE.setLetter(E)
        val LetterF = BlinkyLetter()
        LetterF.setLetter(F)
        val LetterG = BlinkyLetter()
        LetterG.setLetter(G)
        effect(LetterA)
        effect(LetterB)
        effect(LetterC)
        effect(LetterD)
        effect(LetterE)
        effect(LetterF)
        effect(LetterG)
        skip(100)
        LetterA.cancel()
        LetterB.cancel()
        LetterC.cancel()
        LetterD.cancel()
        LetterE.cancel()
        LetterF.cancel()
        LetterG.cancel()

        for (letter in letters) {
            rgbFade(letter, 255, 0, 0, 50)
        }

        skip(100)
    }

    private inner class BlinkyLetter : EffectBase() {
        private var Letter: BulbSet? = null
        private var cancelled = false
        private var timeOut = false
        private var time = 0
        private val timeOutTime = 100
        private val random = Random(System.currentTimeMillis())

        override suspend fun run() {
            while (!(cancelled || timeOut)) {
                set(Letter!!, 255, 147, 41)
                next()
                skip(Math.min(Math.exp((-random.nextInt(20) + Math.round(0.2 * time)).toDouble()).toInt(), 100))
                set(Letter!!, 0, 0, 0)
                time = time + 1

                if (time == timeOutTime) {
                    timeOut = true
                    time = 0
                }
                next()
            }
            set(Letter!!, 255, 147, 41)
        }

        fun setLetter(Letter: BulbSet) {
            this.Letter = Letter
        }

        override fun toString() = "BlinkyLetter"

        override fun cancel() {
            cancelled = true
        }
    }
}
