package darkness.generator.scripts.uka21

import java.awt.Color

// Meant to be run with --single-step
class BulbTest : BaseScript() {
    override suspend fun run() {
        super.run()
        
        for(bulb in allBulbs){
            set(bulb, 255, 0, 0)
            next()
            set(bulb, 0, 255, 0)
            next()
            set(bulb, 0, 0, 255)
            next()
            set(bulb, 255, 255, 255)
            next()
            set(bulb, 0, 0, 0)
        }
    }
}
