package sbt.serialization

import java.io.{ File, FileOutputStream, BufferedWriter, OutputStreamWriter }
import scala.pickling.Output
import java.nio.charset.Charset

class UFT8FileOutput(file: File) extends Output[String] {
  private[this] val charset = Charset.forName("UTF-8")
  private[this] val writer = {
    if (!file.getParentFile.exists) {
      file.getParentFile.mkdirs()
    }
    new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, false), charset))
  }
  def result(): String =
    ???

  def put(obj: String): this.type = {
    writer.write(obj)
    this
  }

  def close(): Unit = writer.close()
}
