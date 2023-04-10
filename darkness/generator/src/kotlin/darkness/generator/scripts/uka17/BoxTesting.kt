package darkness.generator.scripts.uka17

import java.awt.Color
import java.io.File
import java.io.IOException
import java.util.*

abstract class BoxTesting : BaseScript() {
    override suspend fun run() {
        val boxId = Integer.parseInt(javaClass.simpleName.replace("[a-zA-Z.]".toRegex(), ""))
        val boxFile = File("../simulator/patterns/UKA-17/boxes.txt")
        val connectors = ArrayList<Connector>()
        try {
            val scanner = Scanner(File("../simulator/patterns/UKA-17/boxes.txt"))
            while (scanner.hasNextLine()) {
                val line = scanner.nextLine()
                val items = line.trim { it <= ' ' }.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val boxItems = items[0].split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val box = Integer.parseInt(boxItems[0])
                if (box != boxId) {
                    continue
                }
                val connector = Integer.parseInt(boxItems[1])
                val bulbId = Integer.parseInt(items[1])
                connectors.add(Connector(connector, bulbId))
            }
        } catch (e: IOException) {
            throw RuntimeException(e)
        }

        connectors.sort()
        val colors = arrayOf(Color.RED, Color.GREEN, Color.BLUE)
        for (connector in connectors) {
            val bulb = bulb(connector.bulbId)
            for (color in colors) {
                rgbFade(bulb, color, 10)
                skip(20)
                set(bulb, 0, 0, 0)
            }
        }
    }

    class BoxTesting01 : BoxTesting()
    class BoxTesting02 : BoxTesting()
    class BoxTesting03 : BoxTesting()
    class BoxTesting04 : BoxTesting()
    class BoxTesting05 : BoxTesting()
    class BoxTesting06 : BoxTesting()
    class BoxTesting07 : BoxTesting()
    class BoxTesting08 : BoxTesting()
    class BoxTesting09 : BoxTesting()
    class BoxTesting10 : BoxTesting()
    class BoxTesting11 : BoxTesting()
    class BoxTesting12 : BoxTesting()
    class BoxTesting13 : BoxTesting()
    class BoxTesting14 : BoxTesting()

    private class Connector(val connector: Int, val bulbId: Int) : Comparable<Connector> {
        override fun compareTo(other: Connector): Int {
            return Integer.compare(connector, other.connector)
        }
    }
}
