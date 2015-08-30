package sbt.serialization
package pickler

/** Helper which ensured we elide the type tag for this pickler in our parent. */
trait ElidingUnpickler[T] extends Unpickler[T] {
  override def unpickleEntry(reader: PReader): Any = {
    reader.hintElidedType(tag)
    super.unpickleEntry(reader)
  }
}