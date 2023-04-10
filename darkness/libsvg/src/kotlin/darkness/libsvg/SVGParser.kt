package darkness.libsvg

import org.apache.batik.anim.dom.*
import org.apache.batik.bridge.BridgeContext
import org.apache.batik.bridge.DocumentLoader
import org.apache.batik.bridge.GVTBuilder
import org.apache.batik.bridge.UserAgentAdapter
import org.apache.batik.dom.svg.SVGOMPoint
import org.apache.batik.parser.AWTPathProducer
import org.apache.batik.parser.PathParser
import org.apache.batik.util.XMLResourceDescriptor
import org.w3c.dom.Element
import org.w3c.dom.svg.*
import java.awt.geom.PathIterator
import java.awt.geom.Rectangle2D
import java.io.File
import java.io.IOException
import java.util.*
import kotlin.math.*

/**
 * A class which will parse an SVG file into letter shapes and bulbs.
 *
 * The SVG file must keep to the following design guidelines:
 * - Contain a group called "group_text" containing:
 *   - <path> elements defining all the letters
 * - Optionally contain a group called "group_bulbs" containing:
 *   - <circle> or <ellipse> elements defining each bulb
 *   - <circle id="bulb-42"> can be used to define a specific bulb number
 * - Optionally have a <rectangle> element called background_outline to define a background for the sign
 *   - Optionally have a <desc>path/to/texture.png</desc> subnode to define a rasterized background
 */
class SVGParser(val svgFile: File, val flatness: Float, val maxLineLength: Float, val scale: Float) {
    val letters = mutableListOf<Letter>()

    val backgroundOutline = Rectangle2D.Float()
    var backgroundTexture = ""

    fun parse() {
        if (!svgFile.exists()) {
            throw IOException("File $svgFile does not exist")
        }
        // Windows path hacks
        val extraSlash = if (svgFile.absolutePath.contains(":")) "/" else ""
        val path = svgFile.absolutePath.replace("\\", "/")

        val sax = SAXSVGDocumentFactory(XMLResourceDescriptor.getXMLParserClassName())
        val doc = sax.createSVGDocument("file://$extraSlash$path")

        // Create a context to make transforms work
        val userAgent = UserAgentAdapter()
        val loader = DocumentLoader(userAgent)
        val bridgeContext = BridgeContext(userAgent, loader)
        bridgeContext.setDynamicState(BridgeContext.DYNAMIC)

        // Enable CSS- and SVG-specific enhancements.
        GVTBuilder().build(bridgeContext, doc)

        // Get the root element
        val root = doc.rootElement as SVGOMSVGElement

        // Search for a node named group_text which will contain
        // all graphical elements related to the text outline of the sign
        val textGroup = root.getElementById("group_text")
            ?: throw IOException("SVG does not contain a group with the id 'group_text'")


        // The letters outline will consist of a series of closed polygons

        val allBulbs = mutableMapOf<Int, Point>()
        val bulbsWithoutId = mutableListOf<Pair<Letter, Point>>()

        // Iterate all the <path> elements under the text group
        val letterGroups = textGroup.getElementsByTagName("g")
        for (i in 0 until letterGroups.length) {
            val letterGroup = letterGroups.item(i) as Element
            val paths = letterGroup.getElementsByTagName("path")
            if (paths.length != 1) throw Exception("There must be exactly one path inside a letter group")
            val path = paths.item(0) as SVGOMPathElement

            // Calculate a transform from the root node to the element
            val transform = path.getTransformToElement(root)
            // Create a path parser to parse the svg path format
            val pp = PathParser()
            val pa = AWTPathProducer()
            pp.pathHandler = pa
            pp.parse(path.getAttribute("d"))

            // Flatten and iterate through the path.
            // Flattening causes the path iterator to only output straight lines
            // We require that the line can not deviate more than 1.0 from the actual position
            // of the point if the line was not straight.
            val pathIterator = pa.shape.getPathIterator(null, flatness.toDouble())

            // Create an empty shape
            val currentLetter = Letter()
            letters.add(currentLetter)
            var currentPath = currentLetter.outline

            // The PathInterator api requires a float array with 6 elements, even tho we only
            // really use the first 2 elements when using a flattened path
            val segmentElements = FloatArray(6)

            // Iterate the entire path
            while (!pathIterator.isDone) {
                val ret = pathIterator.currentSegment(segmentElements)
                pathIterator.next()

                if (ret == PathIterator.SEG_CLOSE) {
                    // Shape closed. Create a new one
                    continue
                } else if (ret == PathIterator.SEG_MOVETO) {
                    // Create a new hole
                    if (currentLetter.outline.isNotEmpty()) {
                        currentPath = ArrayList()
                        currentLetter.holes.add(currentPath)
                    }
                    // Add the point to our shape
                } else if (ret == PathIterator.SEG_LINETO) {
                    // Add the point to our shape
                } else {
                    throw NotImplementedError("The line segment type $ret is not supported")
                }

                // Apply the required transform to the point and add it to the shape
                var p = root.createSVGPoint()
                p.x = segmentElements[0] * 1.0f
                p.y = segmentElements[1] * 1.0f

                // Transform the point to the root coordinate system
                p = p.matrixTransform(transform)
                currentPath.add(Point(p.x * scale, p.y * scale))
            }

            // Iterate all bulbs. Either SVG circles or ellipses
            val circles = letterGroup.getElementsByTagName("circle")
            for (j in 0 until circles.length) {
                val elem = circles.item(j) as SVGOMCircleElement

                val transform = elem.getTransformToElement(root)
                val p = (SVGOMPoint(elem.cx.baseVal.value, elem.cy.baseVal.value) as SVGPoint)
                    .matrixTransform(transform)

                val bulb = Point(p.x * scale, p.y * scale)
                if (elem.id != null && elem.id.startsWith("bulb-")) {
                    val bulbId = elem.id.substringAfter('-').toInt()
                    if (bulbId in allBulbs) {
                        throw IOException("Bulb with id $bulbId is defined twice")
                    }
                    allBulbs[bulbId] = bulb
                    currentLetter.bulbs[bulbId] = bulb
                } else {
                    bulbsWithoutId.add(Pair(currentLetter, bulb))
                }

            }

            val ellipses = letterGroup.getElementsByTagName("ellipse")
            for (i in 0 until ellipses.length) {
                val elem = ellipses.item(i) as SVGOMEllipseElement

                val transform = elem.getTransformToElement(root)
                val p = (SVGOMPoint(elem.cx.baseVal.value, elem.cy.baseVal.value) as SVGPoint)
                    .matrixTransform(transform)

                val bulb = Point(p.x * scale, p.y * scale)
                if (elem.id != null && elem.id.startsWith("bulb-")) {
                    val bulbId = elem.id.substringAfter('-').toInt()
                    if (bulbId in allBulbs) {
                        throw IOException("Bulb with id $bulbId is defined twice")
                    }
                    allBulbs[bulbId] = bulb
                    currentLetter.bulbs[bulbId] = bulb
                } else {
                    bulbsWithoutId.add(Pair(currentLetter, bulb))
                }
            }

        }

        // Order the bulbs. Columns first, highest first
        bulbsWithoutId.sortWith(compareBy({ it.second.x }, { it.second.y }))

        var nextAvailableId = 0
        for ((letter, bulb) in bulbsWithoutId) {
            while (nextAvailableId in allBulbs) {
                nextAvailableId++
            }
            if (nextAvailableId > 1000) {
                throw Exception("Ran out of available bulb ids")
            }
            allBulbs[nextAvailableId] = bulb
            letter.bulbs[nextAvailableId] = bulb
        }

        if (letters.isEmpty()) {
            throw IOException("No text paths found in SVG")
        }

        // Increase the vertex count for longer lines.
        // We do this to ensure a high poly count in the resulting mesh to make lighting look good.
        for (shape in letters) {
            val newOutline = expandPath(shape.outline, maxLineLength)
            shape.outline.clear()
            shape.outline.addAll(newOutline)
            for (i in 0 until shape.holes.size) {
                shape.holes[i] = expandPath(shape.holes[i], maxLineLength)
            }
        }


        // Find the background outline if it exists
        val backgroundOutlineElement = root.getElementById("background_outline")
        when (backgroundOutlineElement) {
            is SVGRectElement -> {
                val transform = backgroundOutlineElement.getTransformToElement(root)
                val topLeft = SVGOMPoint(
                    backgroundOutlineElement.x.baseVal.value,
                    backgroundOutlineElement.y.baseVal.value).matrixTransform(transform)
                val bottomRight = SVGOMPoint(
                    topLeft.x + backgroundOutlineElement.width.baseVal.value,
                    topLeft.y + backgroundOutlineElement.height.baseVal.value).matrixTransform(transform)

                backgroundOutline.x = topLeft.x * scale
                backgroundOutline.y = topLeft.y * scale
                backgroundOutline.width = (bottomRight.x - topLeft.x) * scale
                backgroundOutline.height = (bottomRight.y - topLeft.y) * scale

                val backgroundDescElements = backgroundOutlineElement.getElementsByTagName("desc")
                if (backgroundDescElements.length == 1) {
                    this.backgroundTexture = (backgroundDescElements.item(0) as SVGDescElement).textContent
                }
            }
            null -> {
            } // ignore
            else -> throw NotImplementedError("The background outline can only be a rectangle")
        }


        // Calculate the bounding box for the all the points found
        var minX = letters.map { it.boundingBox[0] }.minOrNull()!!
        var minY = letters.map { it.boundingBox[1] }.minOrNull()!!
        var maxX = letters.map { it.boundingBox[0] + it.boundingBox[2] }.maxOrNull()!!
        var maxY = letters.map { it.boundingBox[1] + it.boundingBox[3] }.maxOrNull()!!

        for (bulb in allBulbs.values) {
            minX = min(minX, bulb.x)
            minY = min(minY, bulb.y)
            maxX = max(maxX, bulb.x)
            maxY = max(maxY, bulb.y)
        }

        if (!backgroundOutline.isEmpty) {
            minX = min(minX, backgroundOutline.x)
            minY = min(minY, backgroundOutline.y)
            maxX = max(maxX, backgroundOutline.x + backgroundOutline.width)
            maxY = max(maxY, backgroundOutline.y + backgroundOutline.height)
        }

        // Use center of x axis
        var cx = minX + (maxX - minX) / 2.0f
        // Use the bottommost point for the Y axis
        var cy = maxY

        val centerElement = root.getElementById("center")
        if (centerElement != null) {
            val transform = (centerElement as SVGLocatable).getTransformToElement(root)
            val p: SVGPoint = when (centerElement) {
                is SVGCircleElement ->
                    SVGOMPoint(centerElement.cx.baseVal.value, centerElement.cy.baseVal.value).matrixTransform(transform)
                is SVGEllipseElement ->
                    SVGOMPoint(centerElement.cx.baseVal.value, centerElement.cy.baseVal.value).matrixTransform(transform)
                else -> throw NotImplementedError("The center element is not a circle nor a ellipse")
            }
            cx = p.x * scale
            cy = p.y * scale
        }

        // Move all the points to be referenced to the new center point origo
        for (letter in letters) {
            for (i in 0 until letter.outline.size) {
                val p = letter.outline[i]
                letter.outline[i] = Point(p.x - cx, p.y - cy)
            }

            for (hole in letter.holes) {
                for (i in 0 until hole.size) {
                    val p = hole[i]
                    hole[i] = Point(p.x - cx, p.y - cy)
                }
            }

            for ((bulbId, p) in letter.bulbs.toMap()) {
                val point = Point(p.x - cx, p.y - cy)
                allBulbs[bulbId] = point
                letter.bulbs[bulbId] = point
            }
        }



        if (!backgroundOutline.isEmpty) {
            backgroundOutline.x -= cx
            backgroundOutline.y -= cy
        }

    }


    private fun expandLine(p1: Point, p2: Point, maxLength: Float): List<Point> {
        val output = mutableListOf<Point>();

        // Calculate the length of the line
        val totalLen = sqrt((p1.x - p2.x).pow(2) + (p1.y - p2.y).pow(2))
        if (totalLen <= maxLength) {
            // No need to do anything fancy
            return output
        }

        // Calculate the angle of the line segment
        val dx = p2.x - p1.x
        val dy = p2.y - p1.y

        // How many times must we split it to get within our max length per segment
        val parts = (totalLen / maxLength).nextUp().toInt()

        // How much should we jump for each part
        val pxLen = dx / parts.toFloat()
        val pyLen = dy / parts.toFloat()

        // Interpolate the line
        for (i in 1 until parts) {
            val x = p1.x + pxLen * i
            val y = p1.y + pyLen * i

            output.add(Point(x, y))
        }

        return output
    }

    private fun expandPath(points: List<Point>, maxLength: Float): MutableList<Point> {
        val output = mutableListOf<Point>()
        for (index in 0 until points.size) {
            val p1 = points[index]
            val p2 = points[(index + 1) % points.size] // If we are at the last point, we should close the loop

            output.add(p1)
            val extraPoints = expandLine(p1, p2, maxLength)
            output.addAll(extraPoints)

            if (index != points.size - 1) {
                output.add(p2)
            }
        }

        return output
    }

    class Point(val x: Float, val y: Float)

    class Letter {
        val outline = mutableListOf<Point>()
        val holes = mutableListOf<MutableList<Point>>()
        val bulbs = mutableMapOf<Int, Point>()

        val boundingBox: FloatArray
            get() {
                val minX = outline.map { it.x }.minOrNull()!!
                val minY = outline.map { it.y }.minOrNull()!!
                val maxX = outline.map { it.x }.maxOrNull()!!
                val maxY = outline.map { it.y }.maxOrNull()!!

                return floatArrayOf(minX, minY, maxX - minX, maxY - minY)
            }
    }
}



