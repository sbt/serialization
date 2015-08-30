package sbt.serialization

import scala.pickling.Generated
import scala.util.Try
import java.io.File

/** A layer of serialization cake which provides the gen* macros for auto-constructing new picklers. */
trait SerializationFunctions {
  import scala.language.experimental.macros

  // non-implicit aliases of pickling's gen macros
  //def genPickler[T]: Pickler[T] with Generated = macro scala.pickling.generator.Compat.genPickler_impl[T]
  //def genUnpickler[T]: Unpickler[T] with scala.pickling.Generated = macro macro scala.pickling.generator.Compat.genUnpickler_impl[T]

  def toJsonString[A: Pickler](a: A): String = SerializedValue(a).toJsonString
  def toJsonFile[A: Pickler](a: A, file: File): Unit = SerializedValue(a).toJsonFile(file)
  def fromJsonString[A: Unpickler](json: String): Try[A] = SerializedValue.fromJsonString(json).parse[A]
  def fromJsonFile[A: Unpickler](file: File): Try[A] = SerializedValue.fromJsonFile(file).parse[A]
}
