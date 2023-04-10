package darkness.generator.scripts.uka15

import java.awt.Color
import java.util.*

class MiniLysreklamen : BaseScript() {
    override suspend fun run() {
        super.run()

        randomBulbs()
        fadeIn()

        skip(4)

        for (letter in letters) {
            rgbFade(letter, 255, 255, 255, 4)
            skip(4)
        }

        skip(16)

        rainbow(64)
        turnOnSequentially()

        skip(64)

        /*
		skip(16);

		// Circus sign
		{
			int i = 0;

			// Fade each bulb to a semi-random colour
			for( int j = 0; j < 32; ++j) {
				for (BulbGroup letter : letters) {
					for (BulbRGB bulb : letter) {
						float col[] = {i * 0.15f % 1.0f, 0.4f, 0.8f};
						//hsbFade(bulb, col, 16);
						setHSB(bulb, col[0], col[1], col[2]);
						i++;
					}
				}
				next();
			}
		}

		// Horizontal rainbow
		int numColors = columns.length;
		Color colors[] = new Color[numColors];

		for (int i = 0; i < columns.length; ++i) {
			colors[i] = Color.getHSBColor((1.0f/numColors)*i, 0.4f, 0.8f);
		}

		for (int i = 0; i < 64; ++i) {
			int j = 0;
			for (BulbGroup col : columns) {
				for (BulbRGB bulb : col) {
					set(bulb, colors[(i + j) % numColors]);
				}
				++j;
			}
			next();
		}

		skip(32);
		*/
    }

    private suspend fun randomBulbs() {
        // Show some (semi)random bulbs in each letter
        val r = Random()

        val kindaRandom = listOf(C, F, A, D, G, E, B)

        for (i in 0..2) {

            for (letter in kindaRandom) {
                val bulb = letter.getBulb(r.nextInt(letter.numBulbs))
                setHSB(bulb, r.nextFloat(), 0.6f, 0.7f)
                next()
                set(bulb, 0, 0, 0)
                if (i < 2) {
                    skip(2 - i)
                }
            }
        }
    }

    private suspend fun fadeIn() {
        val kindaRandom = listOf(C, F, A, D, G, E, B)

        var i = 0

        for (letter in kindaRandom) {
            val col = floatArrayOf(i / 6f, 0.6f, 0.6f)
            rgbFade(letter, 255, 255, 255, 4)
            skip(4)

            hsbFade(letter, col, 4)
            skip(8)

            ++i
        }
    }

    private suspend fun rainbow(frames: Int) {
        // Horizontal rainbow
        val numColors = columns.size
        val colors = mutableListOf<Color>()

        for (i in 0 until numColors) {
            colors.add(Color.getHSBColor(1.0f / numColors * i, 0.4f, 0.8f))
        }

        for (i in 0 until frames) {
            for ((j, col) in columns.withIndex()) {
                for (bulb in col) {
                    set(bulb, colors[(i + j) % numColors])
                }
            }
            next()
        }
    }

    private suspend fun turnOnSequentially() {
        for (letter in letters) {
            set(letter, 0, 0, 0)
        }

        // Turn each bulb on one by one
        for (letter in letters) {
            for (bulb in letter) {
                set(bulb, Color.WHITE)
                next()
            }
        }
    }
}
