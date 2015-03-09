package sbt.serialization
package pickler

import scala.pickling.{ FastTypeTag, PBuilder, PReader }
import scala.pickling.pickler.PrimitivePicklers

trait Tuple2Picklers extends PrimitivePicklers with RichTypes {
  implicit def tuple2Pickler[T1: FastTypeTag, T2: FastTypeTag](implicit elem1Pickler: Pickler[T1],
    elem1Unpickler: Unpickler[T1],
    elem2Pickler: Pickler[T2],
    elem2Unpickler: Unpickler[T2],
    collTag: FastTypeTag[(T1, T2)]): Pickler[(T1, T2)] with Unpickler[(T1, T2)] =
    new Pickler[(T1, T2)] with Unpickler[(T1, T2)] {

      val tag = collTag
      private implicit val elem1Tag = implicitly[FastTypeTag[T1]]
      private implicit val elem2Tag = implicitly[FastTypeTag[T2]]

      def pickle(coll: (T1, T2), builder: PBuilder): Unit = {
        // Our type should already be hinted before this method, however we additionally mark our type as
        // statically elided.
        builder.hintStaticallyElidedType()
        builder.beginEntry(coll)
        builder.beginCollection(2)
        builder.putElement { b =>
          b.hintTag(elem1Tag)
          elem1Pickler.pickle(coll._1, b)
        }
        builder.putElement { b =>

          b.hintTag(elem2Tag)
          elem2Pickler.pickle(coll._2, b)
        }
        builder.endCollection()
        builder.endEntry()
      }

      def unpickle(tag: String, preader: PReader): Any = {
        preader.beginCollection()
        // TODO - better warning here.
        assert(preader.readLength() == 2)
        val fst = elem1Unpickler.unpickleEntry(preader.readElement())
        val snd = elem2Unpickler.unpickleEntry(preader.readElement())
        preader.endCollection()
        (fst, snd)
      }
    }
}
