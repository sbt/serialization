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

      val tag = implicitly[FastTypeTag[(T1, T2)]]
      private implicit val elem1Tag = implicitly[FastTypeTag[T1]]
      private implicit val elem2Tag = implicitly[FastTypeTag[T2]]

      def pickle(coll: (T1, T2), builder: PBuilder): Unit = {
        builder.beginEntry(coll)
        builder.putField("_1", { b =>
          b.hintTag(elem1Tag)
          b.hintStaticallyElidedType()
          elem1Pickler.pickle(coll._1, b)
        })
        builder.putField("_2", { b =>
          b.hintTag(elem2Tag)
          b.hintStaticallyElidedType()
          elem2Pickler.pickle(coll._2, b)
        })
        builder.endEntry()
      }

      def unpickle(tag: String, preader: PReader): Any = {
        val fst = elem1Unpickler.unpickleEntry(preader.readField("_1"))
        val snd = elem2Unpickler.unpickleEntry(preader.readField("_2"))
        (fst, snd)
      }
    }
}
