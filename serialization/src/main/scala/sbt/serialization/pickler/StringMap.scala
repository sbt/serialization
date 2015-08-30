package sbt.serialization
package pickler

import scala.collection.generic.CanBuildFrom
import scala.pickling._

trait StringMapPicklers {
  // FIXME this could theoretically work for M<:Map[String,A] and use a CanBuildFrom for M?
  implicit def stringMapPickler[A](implicit valuePickler: Pickler[A], valueUnpickler: Unpickler[A], valueTag: FastTypeTag[A],
    mapTag: FastTypeTag[Map[String, A]],
    keysPickler: Pickler[List[String]], keysUnpickler: Unpickler[List[String]]): Pickler[Map[String, A]] with Unpickler[Map[String, A]] = new AbstractPicklerUnpickler[Map[String, A]] with ElidingUnpickler[Map[String, A]] {
    override val tag = mapTag

    def pickle(m: Map[String, A], builder: PBuilder): Unit = {
      builder.pushHints()
      builder.hintElidedType(mapTag)
      builder.beginEntry(m, mapTag)
      // This is a pseudo-field that the JSON format will ignore reading, but
      // the binary format WILL write.
      // TODO - We should have this be a "hintDynamicKeys" instead.
      builder.putField("$keys", { b =>
        keysPickler.pickle(m.keys.toList.sorted, b)
      })
      m foreach { kv =>
        builder.putField(kv._1, { b =>
          valuePickler.pickle(kv._2, b)
        })
      }
      builder.endEntry()
      builder.popHints()
    }
    // TODO - We need to hint our elid before we get here...
    def unpickle(tpe: String, reader: PReader): Any = {
      reader.pushHints()
      reader.hintElidedType(tag)
      reader.beginEntry()
      val keys = {
        val r = reader.readField("$keys")
        r.hintElidedType(keysUnpickler.tag)
        keysUnpickler.unpickleEntry(r).asInstanceOf[List[String]]
      }
      val results = for (key <- keys) yield {
        val value = valueUnpickler.unpickleEntry(reader.readField(key))
        key -> value.asInstanceOf[A]
      }
      reader.endEntry()
      reader.popHints()
      results.toMap
    }
    override def toString = "StringMapPicklerUnpickler"
  }
}
