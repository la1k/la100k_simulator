package darkness.libsvg

import java.awt.*
import java.awt.geom.Ellipse2D
import javax.swing.JPanel

/**
 * A JPanel displaying the parser result graphically in a UI.
 */
class Visualizer(private val parser: SVGParser) : JPanel() {
    private val RULER_LENGTH = 1200
    private val RULER_HEIGHT = 300
    private val CENTER_X = 100 + RULER_LENGTH / 2
    private val CENTER_Y = 400 + RULER_HEIGHT
    private val COLOR_STROKE = Color(0, 0, 0)
    private val COLOR_VERTICES = Color(255, 0, 0)
    private val COLOR_BULB = Color(0, 0, 255)
    private val COLOR_BACKGROUND_OUTLINE = Color(95, 255, 95)

    private fun drawOutline(g2: Graphics2D, points: List<SVGParser.Point>) {
        val npoints = points.size
        val xpoints = IntArray(npoints)
        val ypoints = IntArray(npoints)

        for (i in 0 until npoints) {
            xpoints[i] = points[i].x.toInt() + CENTER_X
            ypoints[i] = points[i].y.toInt() + CENTER_Y
        }

        val poly = Polygon(xpoints, ypoints, npoints)
        g2.color = COLOR_STROKE
        g2.stroke = BasicStroke(2.0f)
        g2.draw(poly)

        g2.color = COLOR_VERTICES
        for (i in 0 until npoints) {
            g2.draw(Ellipse2D.Float(xpoints[i].toFloat(), ypoints[i].toFloat(), 4.0f, 4.0f))
        }
    }

    private fun drawRuler(g2: Graphics2D) {
        val startX = CENTER_X - RULER_LENGTH / 2 - 20
        val startY = CENTER_Y + 20
        val length = RULER_LENGTH
        val height = RULER_HEIGHT
        g2.color = Color.BLACK
        g2.stroke = BasicStroke(2.0f)
        g2.drawLine(startX, startY, startX + length, startY)


        for (offX in 0..length step 100) {
            g2.drawLine(startX + offX, startY - 10, startX + offX, startY + 10)
            g2.drawString("${offX - RULER_LENGTH / 2}cm", startX + offX, startY + 20)
        }

        g2.drawLine(startX, startY, startX, startY - height)
        for (offY in 0..height step 100) {
            g2.drawLine(startX - 10, startY - offY, startX + 10, startY - offY)
            g2.drawString("${offY}cm", startX - 55, startY - offY)
        }

    }


    public override fun paintComponent(g: Graphics) {
        val g2 = g as Graphics2D

        drawRuler(g2)

        if (!parser.backgroundOutline.isEmpty) {
            g2.color = COLOR_BACKGROUND_OUTLINE
            g2.stroke = BasicStroke(2.0f)
            g2.drawRect(CENTER_X + parser.backgroundOutline.x.toInt(),
                CENTER_Y + parser.backgroundOutline.y.toInt(),
                parser.backgroundOutline.width.toInt(),
                parser.backgroundOutline.height.toInt())
        }

        for (letter in parser.letters) {

            g2.color = Color.LIGHT_GRAY
            g2.stroke = BasicStroke(2.0f)
            val bb = letter.boundingBox
            g2.drawRect(CENTER_X + bb[0].toInt(), CENTER_Y + bb[1].toInt(), bb[2].toInt(), bb[3].toInt())

            drawOutline(g2, letter.outline)
            for (hole in letter.holes) {
                drawOutline(g2, hole)
            }
            for ((bulbId, bulb) in letter.bulbs.entries) {
                g2.color = COLOR_BULB
                g2.draw(Ellipse2D.Float(CENTER_X + bulb.x, CENTER_Y + bulb.y, 4.0f, 4.0f))
                g2.drawString("$bulbId", CENTER_X + bulb.x, CENTER_Y + bulb.y)
            }
        }


    }
}
