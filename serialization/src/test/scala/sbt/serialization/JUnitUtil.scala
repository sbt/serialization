package sbt.serialization.spec

import org.junit.Assert._
import org.junit._
import sbt.serialization._

object JUnitUtil {
  private def addWhatWeWerePickling[T, U](t: T)(body: => U): U = try body
  catch {
    case e: Throwable =>
      e.printStackTrace()
      throw new AssertionError(s"Crash round-tripping ${t.getClass.getName}: value was: ${t}", e)
  }

  def roundTripArray[A](x: Array[A])(implicit ev0: Pickler[Array[A]], ev1: Unpickler[Array[A]]): Unit =
    roundTripBase[Array[A]](x)((a, b) =>
      assertEquals(a.toList, b.toList)) { (a, b) =>
      assertEquals(s"Failed to round trip $x via ${implicitly[Pickler[Array[A]]]} and ${implicitly[Unpickler[Array[A]]]}", a.getMessage, b.getMessage)
    }
  def roundTrip[A: Pickler: Unpickler](x: A): Unit =
    roundTripBase[A](x)((a, b) =>
      assertEquals(a, b)) { (a, b) =>
      assertEquals(s"Failed to round trip $x via ${implicitly[Pickler[A]]} and ${implicitly[Unpickler[A]]}", a.getMessage, b.getMessage)
    }
  def roundTripBase[A: Pickler: Unpickler](a: A)(f: (A, A) => Unit)(e: (Throwable, Throwable) => Unit): Unit = addWhatWeWerePickling(a) {
    val json = toJsonString(a)
    //System.err.println(s"json: $json")
    val parsed = fromJsonString[A](json).get
    (a, parsed) match {
      case (a: Throwable, parsed: Throwable) => e(a, parsed)
      case _                                 => f(a, parsed)
    }
  }
  implicit class AnyOp[A](a: A) {
    def must_==(b: A): Unit = assertEquals(b, a)
  }

  import scala.language.implicitConversions
  import sbt.serialization.json.JSONPickle
  import scala.pickling.UnpickleOps
  implicit def toJSONPickle(value: String): JSONPickle = JSONPickle(value)
  implicit def toUnpickleOps(value: String): UnpickleOps = new UnpickleOps(JSONPickle(value))
}
