package darkness.libsvg

import java.awt.Dimension
import java.io.File
import javax.swing.JFrame
import javax.swing.WindowConstants

/**
 * Entry point of the standalone build of libsvg
 *
 * Takes the arguments
 * --flatten <float> : the minimum distance a flattened curve can diverge from the real smooth curve
 * --splitlines <float> : the maximum distance between points in the outline of the letters
 * -o <filename> : the filename to write a pattern file to
 * -v : Display a UI of the parsed SVG
 * -s <scale> or --scale <scale> : scale the SVG by the given factor
 * <filename.svg> : the SVG file to parse
 */
class Entry {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            var fileName: String? = null
            var outputFileName: String? = null
            var flatness = 10.0f
            var maxLineLength = 10.0f
            var visualize = false
            var scale = 1.0f

            var index = 0
            while (index < args.size) {
                val arg = args[index]
                if (arg == "--flatten" && index + 1 < args.size) {
                    flatness = args[++index].toFloat()
                } else if (arg == "--splitlines" && index + 1 < args.size) {
                    maxLineLength = args[++index].toFloat()
                } else if (arg == "-o" && index + 1 < args.size) {
                    outputFileName = args[++index]
                } else if (arg == "-v") {
                    visualize = true
                } else if ((arg == "--scale" || arg == "-s") && index + 1 < args.size) {
                    scale = args[++index].toFloat()
                } else {
                    if (fileName != null) {
                        error("Filename can not be provided twice")
                    }
                    fileName = arg
                }
                index++
            }

            if (fileName == null) {
                error("Filename for the target SVG must be provided")
            }

            // Load the SVG file into Batik
            val parser = SVGParser(File(fileName), flatness, maxLineLength, scale)
            parser.parse()

            val generator = PatternGenerator(parser)
            val pattern = generator.generate()
            println(pattern)
            outputFileName?.let { File(it).writeText(pattern) }

            if (visualize) {
                with(JFrame()) {
                    title = "SVG Visualizer"
                    size = Dimension(640, 480)
                    contentPane.add(Visualizer(parser))
                    defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
                    isVisible = true
                }
            }
        }
    }
}
