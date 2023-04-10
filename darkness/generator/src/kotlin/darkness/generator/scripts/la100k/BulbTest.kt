package darkness.generator.scripts.la100k

class BulbTest : BaseScript() {
    override suspend fun run() {
        super.run()
        rgbFade(allBulbsGroup,uke_rød,20)
    }
}
