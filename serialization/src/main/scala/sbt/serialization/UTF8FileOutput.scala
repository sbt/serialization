package sbt.serialization

import java.io.{ File, FileOutputStream, BufferedWriter, OutputStreamWriter }
import scala.pickling.Output
import java.nio.charset.Charset

private[serialization] class UFT8FileOutput(file: File) extends Output[String] {
  private[this] val writer = {
    if (!file.getParentFile.exists) {
      file.getParentFile.mkdirs()
    }
    new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, false), UFT8FileOutput.utf8))
  }
  def result(): String = throw new UnsupportedOperationException()

  def put(obj: String): this.type = {
    writer.write(obj)
    this
  }

  def close(): Unit = writer.close()
}

private[serialization] object UFT8FileOutput {
  val utf8 = Charset.forName("UTF-8")
}
