package sbt.serialization
package pickler

trait SerializationPicklers {
  implicit val serializedValuePickler: Pickler[SerializedValue] with Unpickler[SerializedValue] = SerializedValue.pickler
}
