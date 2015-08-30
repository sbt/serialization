package sbt.serialization
package pickler

import scala.pickling._
// TODO - Why is alias not working.
import scala.pickling.pickler.PrimitivePicklers

trait OptionPicklers {
  implicit def optionPickler[A: FastTypeTag](implicit elemPickler: Pickler[A], elemUnpickler: Unpickler[A], collTag: FastTypeTag[Option[A]]): AbstractPicklerUnpickler[Option[A]] =
    new AbstractPicklerUnpickler[Option[A]] with ElidingUnpickler[Option[A]] {
      private implicit val elemTag = implicitly[FastTypeTag[A]]
      val tag = implicitly[FastTypeTag[Option[A]]]
      private val nullTag = implicitly[FastTypeTag[Null]]
      def pickle(coll: Option[A], builder: PBuilder): Unit = {
        // Here we cheat the "entry" so that the notion of option
        // is erased for "null"
        coll match {
          case Some(elem) =>
            builder.beginEntry(coll, tag)
            builder.beginCollection(1)
            builder.putElement { b =>
              b.hintElidedType(elemTag)
              elemPickler.pickle(elem, b)
            }
            builder.endCollection()
            builder.endEntry()
          case None =>
            // TODO - Json Format shoudl special case this.
            builder.beginEntry(None, tag)
            builder.beginCollection(0)
            builder.endCollection()
            builder.endEntry()
        }
      }
      def unpickle(tag: String, preader: PReader): Any = {
        // Note - if we call beginEntry we should see JNothing or JNull show up if the option is empty.
        val reader = preader.beginCollection()
        reader.hintElidedType(elemTag)
        val length = reader.readLength
        val result: Option[A] =
          if (length == 0) None
          else {
            val elem = elemUnpickler.unpickleEntry(reader.readElement())
            Some(elem.asInstanceOf[A])
          }
        reader.endCollection()
        result
      }
    }
}