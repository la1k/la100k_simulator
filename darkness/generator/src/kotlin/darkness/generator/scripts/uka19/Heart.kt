package darkness.generator.scripts.uka19

import darkness.generator.api.BulbGroup

class Heart : BaseScript() {
    override suspend fun run() {
        super.run()

        for (letter in letters) {
            set(letter, 255, 196, 0)
        }

        set(C, 100, 0, 0)
        for (i in 1..10) {
            rgbFade(C, 255, 0, 0, 2)
            skip(3)
	    rgbFade(C, 140, 0, 0, 4)
	    skip(2)

            skip(2)
	    rgbFade(C, 220, 0, 0, 2)
            skip(1)
            rgbFade(B, 255, 0, 0, 4)
            rgbFade(D, 255, 0, 0, 4)
	    skip(2)
            
            rgbFade(C, 170, 0, 0, 4)
            skip(1)
	    rgbFade(C, 100, 0, 0, 20)
	    skip(1)
	    rgbFade(A, 255, 0, 0, 4)
            rgbFade(E, 255, 0, 0, 4)
	    skip(3)

            skip(1)
            rgbFade(B, 255, 196, 0, 6)
	    rgbFade(D, 255, 196, 0, 6)
            rgbFade(F, 255, 0, 0, 4)
            skip(3)
            skip(1)
            
            rgbFade(G, 255, 0, 0, 4)
            rgbFade(A, 255, 196, 0, 6)            
            rgbFade(E, 255, 196, 0, 6)
            skip(3)
            skip(1)
            rgbFade(F, 255, 196, 0, 6)
	    skip(1)
            
            skip(2)
            skip(1)
	    rgbFade(G, 255, 196, 0, 6)
            skip(2)

            skip(5)
        }
    }
}
