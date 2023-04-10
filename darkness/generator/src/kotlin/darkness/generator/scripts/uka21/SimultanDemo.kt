package darkness.generator.scripts.uka21

class SimultanDemo : BaseScript() {
    override suspend fun run() {
        super.run()

        for(i in 0 until allBulbs.size/2){
            set(allBulbs[i], 255, 0, 0)
            set(allBulbs[allBulbs.size-1-i], 255, 255, 0)
            skip(2)
        }
        
        for(i in 0 until allBulbs.size/2){
            set(allBulbs[allBulbs.size/2+i], 255, 128, 0)
            set(allBulbs[allBulbs.size/2-i], 153, 76, 0)
            skip(2)
        }

        for(i in 0 until allBulbs.size/2){
            set(allBulbs[i], 255, 255, 0)
            set(allBulbs[allBulbs.size-1-i], 0, 0, 255)
            skip(2)
        }
        
        for(i in 0 until allBulbs.size/2){
            set(allBulbs[allBulbs.size/2+i], 51, 255, 51)
            set(allBulbs[allBulbs.size/2-i], 0, 153, 0)
            skip(2)
        }

        for(i in 0 until allBulbs.size/2){
            set(allBulbs[i], 255, 0, 255)
            set(allBulbs[allBulbs.size-1-i], 255, 255, 0)
            skip(2)
        }
        
        for(i in 0 until allBulbs.size/2){
            set(allBulbs[allBulbs.size/2+i], 255, 0, 0)
            set(allBulbs[allBulbs.size/2-i], 255, 0, 0)
            skip(2)
        }
        for (bulb in A) {
                set(bulb, 255, 0, 0)
                skip(2)
            }
        skip(200)
    }
}
