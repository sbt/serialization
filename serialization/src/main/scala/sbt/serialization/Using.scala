package sbt.serialization

import java.io.{ File, FileOutputStream, BufferedWriter, OutputStreamWriter }
import java.nio.charset.Charset

object Using {
  def fileWriter(file: File, charset: Charset = Charset.forName("UTF-8"), append: Boolean = false)(f: BufferedWriter => Unit): Unit =
    {
      if (!file.getParentFile.exists) {
        file.getParentFile.mkdirs()
      }
      val resource = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, append), charset))
      try {
        f(resource)
      } finally {
        resource.close()
      }
    }
}
