package sbt.serialization

import scala.pickling.Generated
import scala.util.Try

/** A layer of serialization cake which provides the gen* macros for auto-constructing new picklers. */
trait SerializationFunctions {
  import scala.language.experimental.macros

  // non-implicit aliases of pickling's gen macros
  def genPickler[T]: Pickler[T] = macro scala.pickling.Compat.PicklerMacros_impl[T]
  def genUnpickler[T]: Unpickler[T] with scala.pickling.Generated = macro scala.pickling.Compat.UnpicklerMacros_impl[T]

  def toJsonString[A: Pickler](a: A): String = SerializedValue(a).toJsonString
  def fromJsonString[A: Unpickler](json: String): Try[A] = SerializedValue.fromJsonString(json).parse[A]
}
