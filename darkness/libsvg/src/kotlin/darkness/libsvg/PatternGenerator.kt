package darkness.libsvg

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter.Indenter
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

/**
 * This class, along with [PatternGroup] and [PatternAlu], match the structure of the JSON file
 * format that is expected by websim for the pattern file.
 */
private data class Pattern(
    val formatVersion: String,
    val groups: List<PatternGroup>,
)

private data class PatternGroup(
    val pos: List<Float>,
    val alu: PatternAlu,
    val bulbs: List<List<Any>>,
)

private data class PatternAlu(
    val outline: List<List<Float>>,
    val holes: List<List<List<Float>>>,
)

// This is somewhat unnecessary, but it makes the output JSON look nicer than the default pretty
// printing that Jackson offers.
private val prettyPrinter = DefaultPrettyPrinter()
    .withArrayIndenter(
        object : Indenter {
            override fun writeIndentation(g: JsonGenerator, level: Int) {
                val list = g.currentValue as? List<*>
                // This function is called before every array element is printed, and we get to
                // check what is in the array that we're printing. We want to write points
                // (two floats) and bulbs (six elements, where the first one is an int) on a single
                // line, but write all other arrays with each element on its own line and indented.
                if (list?.size == 2 && list.all { it is Float } || list?.size == 6 && list[0] is Int) {
                    g.writeRaw(' ')
                } else {
                    g.writeRaw("\n" + "  ".repeat(level))
                }
            }
            override fun isInline() = false
        },
    )

/** This is the object that can convert Kotlin objects to JSON. */
private val jsonMapper = jacksonObjectMapper().writer(prettyPrinter)

/**
 * This class accepts an [SVGParser] that has already parsed an SVG file. Calling [generate] will
 * return a JSON file for the pattern, in the format that is expected by websim.
 */
class PatternGenerator(private val parser: SVGParser) {
    fun generate(): String {
        val pattern = Pattern(
            formatVersion = "v0.1.0",
            groups = parser.letters.map { letter ->
                PatternGroup(
                    pos = listOf(0.0f, 0.0f),
                    alu = PatternAlu(
                        outline = letter.outline.map { point -> listOf(point.x, -point.y) },
                        holes = letter.holes.map { hole ->
                            hole.map { point -> listOf(point.x, -point.y) }
                        },
                    ),
                    bulbs = letter.bulbs.map { (bulbId, position) ->
                        val (redChannel, greenChannel, blueChannel) = createChannels(bulbId)
                        listOf(bulbId, position.x, -position.y, redChannel, greenChannel, blueChannel)
                    },
                )
            },
        )

        val json = jsonMapper.writeValueAsString(pattern)
        // Unfortunately, Jackson generates Windows line endings "\r\n", so we delete all the "\r"
        return json.replace("\r", "")
    }
}

/**
 * Here, we autogenerate DMX channel numbers based on the bulb ids, in a way that is fairly easy to
 * work with mentally. Most signs have less than 100 bulbs, so that is the simplest scheme: the bulb
 * with id x gets the red channel 200+x, the green channel 300+x, and the blue channel 400+x. If any
 * bulb has an id that is greater than 99, we require that it is less than 150; we subtract 100 from
 * the bulb id to get a number 0 <= y < 50, and it gets the red channel y, the green channel y+50,
 * and the blue channel y+100. Thus, we get the following channel setup:
 * Red:       0-49 and 200-299
 * Green:    50-99 and 300-399
 * Blue:   100-149 and 400-499
 * The channels 150-199 and 500-511 will never be assigned, but if need be, we could come up with
 * assignments for those as well if more than 150 bulbs are needed.
 */
private fun createChannels(bulbId: Int): Triple<Int, Int, Int> {
    return when {
        bulbId < 100 -> Triple(200 + bulbId, 300 + bulbId, 400 + bulbId)
        bulbId < 150 -> {
            val shiftedId = bulbId - 100
            Triple(shiftedId + 0, shiftedId + 50, shiftedId + 100)
        }
        else -> throw Exception(
            "Bulb id $bulbId is not supported for pattern generation (must be less than 150)",
        )
    }
}
