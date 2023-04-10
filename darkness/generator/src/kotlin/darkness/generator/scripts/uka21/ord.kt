package darkness.generator.scripts.uka21

import darkness.generator.api.effects.PointRainbow
import kotlin.math.sin
import java.awt.Color

class ord : BaseScript() {
    override suspend fun run() {
        super.run()

        val red = Color(255, 0, 0)
        val orange = Color(255, 179, 0)

        set(allBulbsGroup, 0, 255, 0)
        skip(20)
        set(A, red)
        set(B, red)
        set(C, red)
        set(D, red)
        skip(10)
        set(E, 24, 193, 188)
        set(F, 24, 193, 188)
        skip(10)
        set(G, orange)
        set(H, orange)
        set(I, orange)
        set(J, orange)
        skip(40)

    

        
    }
}

