package darkness.generator.scripts.uka17

import darkness.generator.api.BulbGroup
import java.awt.Color

class TestSekvens : BaseScript() {
    override suspend fun run() {
        super.run()

        //BulbGroup[] bunnGroups = {
        //	group(0, 7, 15, 16, 19, 33, 38, 45, 50, 49, 68, 78, 95),
        //	group(1, 8, 17, 20, 25, 32, 39, 46, 51, 63, 69, 77, 79, 85, 90),
        //	group(2, 9, 18, 21, 26, 31, 37, 47, 52, 55, 62, 67, 76, 82, 80, 86, 91),
        //	group(3, 10, 30, 36, 40, 48, 53, 54, 61, 66, 70, 83, 87, 92),
        //	group(4, 11, 35, 41, 56, 60, 65, 71, 66, 72, 75, 84, 81, 88, 93),
        //	group(6, 34, 42, 73, 89, 94),
        //	group(5, 43),
        //};

        val foo = Color(255, 255, 56)
        foo.red
        foo.blue
        foo.green

        var previousLetter: BulbGroup? = null
        val hei = 0

        for (letter in letters) {
            for (i in 0..9) {
                set(letter, foo)
                skip(1)
                set(letter, 0, 0, 0)
                skip(1)
                if (previousLetter != null) {
                    set(previousLetter, 255, 255, 255)
                }
                previousLetter = letter
            }


            //col1 = col1+3;
            //col2 = col2+3;
            //col3 = col3+3;
            //if (col1 > 255 | col2 > 255 | col3 > 255) {
            //	break;
            //}
        }
    }
}
