package darkness.generator.output

import java.io.FileWriter
import java.io.BufferedWriter

class FileOutput(fileName: String) : BaseOutput(BufferedWriter(FileWriter(fileName))) {
    override fun flush() {
        writer.flush()
    }
}
