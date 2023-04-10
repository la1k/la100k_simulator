package darkness.generator.output

import java.io.BufferedWriter
import java.io.OutputStreamWriter

class ConsoleOutput : BaseOutput(BufferedWriter(OutputStreamWriter(System.out))) {
    override fun nextFrame() {
        // Flush output so we get each frame on a separate line
        flush()
        super.nextFrame()
    }

    override fun flush() {
        writer.flush()
    }

    // Don't close stdout
    override fun close() {}
}
