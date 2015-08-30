package sbt

/**
 * A package object which can be used to create new serializers.
 *
 * This package supports creating Pickler/Unpickler functions which can serialize arbitrary types.  See the
 * SerializedValue type for what formats this library supports serializing into.
 */
package object serialization extends SerializationFunctions with CustomPicklers {
  type Pickler[A] = scala.pickling.Pickler[A]
  val Pickler = scala.pickling.Pickler
  type Unpickler[A] = scala.pickling.Unpickler[A]
  val Unpickler = scala.pickling.Unpickler
  val PicklerUnpickler = scala.pickling.PicklerUnpickler
  // These are exposed for custom implementations of picklers.
  type FastTypeTag[A] = scala.pickling.FastTypeTag[A]
  type PReader = scala.pickling.PReader
  type PBuilder = scala.pickling.PBuilder

  // pickling macros need FastTypeTag$ to have been initialized;
  // if things ever compile with this removed, it can be removed.
  private val __forceInitializeFastTypeTagCompanion = scala.pickling.FastTypeTag

  // All generated picklers are required to be static-only in this library.
  implicit val StaticOnly = scala.pickling.static.StaticOnly
  implicit val ignoreCaseClassSubclasses = scala.pickling.generator.opts.ignoreCaseClassSubclasses

  implicit val ShareNothing = scala.pickling.shareNothing.ShareNothing
  type directSubclasses = _root_.scala.pickling.directSubclasses
}
