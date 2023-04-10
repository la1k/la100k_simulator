package darkness.generator.output

import darkness.generator.api.Channel
import darkness.generator.api.ScriptManager

import java.io.BufferedWriter

/**
 * Instances of subclasses of this class can be passed to [ScriptManager.start],
 * and their methods will be invoked at various points in the generation of a sequence.
 * Thus, a generated sequence may be output in various manners.
 */
abstract class BaseOutput(protected val writer: BufferedWriter) : AutoCloseable {
    private var frameNumber = 1

    /**
     * Is called once per channel per frame; however, it might not be called in the sequence of the channel index.
     * TODO: This essentially means that [ConsoleOutput] and [FileOutput] are useless, and should be fixed.
     */
    open fun writeChannelValue(channel: Channel, value: Int) {
        writer.write(value.toString())
        writer.write(" ")
    }

    open fun writeString(str: String) {
        writer.write(str)
    }

    open fun writeNewline() {
        writeString("\n")
    }

    open fun writeComment(comment: String) {
        writer.write("#$comment\n")
    }

    open fun beginScript() {
        println("Script begin: ")
    }

    open fun beginScript(name: String) {
        println("Script \"$name\" begin: ")
    }

    open fun endScript() {
        println("Script done")
    }

    open fun endScript(name: String) {
        println("Script \"$name\" done")
    }

    open fun beginFrame() {
        print("Frame " + Integer.toString(frameNumber) + ": ")
    }

    open fun endFrame() {
        nextFrame()
    }

    abstract fun flush()

    open fun nextFrame() {
        ++frameNumber
        writeNewline()
    }

    override fun close() {
        writer.close()
    }
}
