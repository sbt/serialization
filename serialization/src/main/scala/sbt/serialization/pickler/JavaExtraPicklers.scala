package sbt.serialization
package pickler

import java.io.File
import java.net.URI
import scala.pickling.{ FastTypeTag, PBuilder, PReader, PicklingException }
// TODO - Why is alias not working.
import scala.pickling.pickler.{ PrimitivePicklers, RefPicklers }

/** Contains implementation-details of "can to strings" for Java/sbt 'raw' types. */
object JavaExtraPicklers {
  private val fileCanToString: CanToString[File] = CanToString(
    _.toURI.toASCIIString, {
      s: String => new File(new URI(s))
    })
  private val uriCanToString: CanToString[URI] = CanToString(
    _.toASCIIString, {
      s: String => new URI(s)
    })

}

/**
 * Picklers relating to additional Java types we'd like to support.
 *
 * THis includes java.io.File, java.net.URI and the sbt "TypeExpression".
 */
trait JavaExtraPicklers extends PrimitivePicklers {
  // TODO - Maybe this shouldn't be implicitly available.
  implicit def canToStringPickler[A: FastTypeTag](implicit canToString: CanToString[A]): Pickler[A] with Unpickler[A] = new Pickler[A] with Unpickler[A] {
    val tag = implicitly[FastTypeTag[A]]
    def pickle(a: A, builder: PBuilder): Unit = {
      builder.pushHints()
      builder.hintElidedType(FastTypeTag.String)
      stringPickler.pickle(canToString.toString(a), builder)
      builder.popHints()
    }
    def unpickle(tag: String, preader: PReader): Any = {
      preader.pushHints()
      preader.hintElidedType(FastTypeTag.String)
      preader.pinHints()
      val s = stringPickler.unpickle(FastTypeTag.String.key, preader).asInstanceOf[String]
      preader.unpinHints()
      preader.popHints()
      try {
        val result = canToString.fromString(s)
        result
      } catch {
        case e: PicklingException => throw e
        case e: Throwable         => throw PicklingException(s""""$s" is not valid ${tag}""", Some(e))
      }
    }
  }

  implicit val filePickler: Pickler[File] with Unpickler[File] =
    canToStringPickler[File](FastTypeTag[File], JavaExtraPicklers.fileCanToString)
  implicit val uriPickler: Pickler[URI] with Unpickler[URI] =
    canToStringPickler[URI](FastTypeTag[URI], JavaExtraPicklers.uriCanToString)
}