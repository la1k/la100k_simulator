package darkness.generator.output

import darkness.generator.api.Channel

import java.io.BufferedWriter
import java.io.FileWriter

private const val numChannels = 512

/**
 * Generates a PGM file from a sequence. PGM is a simple image format, and we let each row in the image
 * represent the channel values in a frame: the value of channel c in frame f is represented by the grayscale value
 * of the pixel at column c-1 in row f. PGM files are text files with the following structure:
 * - First line: the magic string "P2", meaning a grayscale PGM file
 * - Second line: the width (number of channels) and height (number of frames) of the picture
 * - Third line: the grayscale value that represents white; the pixel values will be between 0 and this value, inclusive.
 * This will normally be 255, but if we use the transparency hack, transparency is represented by 256.
 * - Subsequent lines: each line contains the values of the channels in a frame, from beginning to end
 * Because the number of lines must be specified early on, this class stores all of the frames as they are being generated,
 * and only outputs the PGM file at the very end.
 */
class PgmOutput(private val fileName: String) : BaseOutput(BufferedWriter(FileWriter(fileName))) {
    private val frames = mutableListOf<MutableList<Int>>()
    private var hasTransparency: Boolean = false

    override fun beginFrame() {
        frames.add(MutableList(numChannels) { 0 })
    }

    override fun writeChannelValue(channel: Channel, value: Int) {
        frames[frames.size - 1][channel.channel - 1] = value
        if (value == 256) {
            hasTransparency = true
        }
    }

    override fun flush() {
        writer.write("P2\n")
        writer.write(numChannels.toString() + " " + frames.size + "\n")
        writer.write((if (hasTransparency) 256 else 255).toString() + "\n")
        for (frame in frames) {
            for (i in frame.indices) {
                writer.write(frame[i].toString() + " ")
            }
            writer.write("\n")
        }
        writer.flush()
    }

    // Override everything else in order to silence the superfluous output from BaseOutput
    override fun writeString(str: String) {}

    override fun writeNewline() {}
    override fun writeComment(comment: String) {}
    override fun beginScript() {}
    override fun beginScript(name: String) {}
    override fun endScript() {}
    override fun endScript(name: String) {}
    override fun endFrame() {}
    override fun nextFrame() {}
}
