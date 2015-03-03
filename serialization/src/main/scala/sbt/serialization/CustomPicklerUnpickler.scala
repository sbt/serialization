package sbt.serialization

// TODO - Why are type aliases not workign?
import scala.pickling.pickler.{
  PrimitivePicklers,
  PrimitiveArrayPicklers,
  RefPicklers
}

import sbt.serialization.pickler.{
  OptionPicklers,
  ThrowablePicklers,
  VectorPicklers,
  ListPicklers,
  ArrayPicklers,
  SeqPicklers,
  StringMapPicklers,
  Tuple2Picklers,
  JavaExtraPicklers,
  TypeExpressionPicklers,
  SerializationPicklers
}

trait CustomPicklers extends PrimitivePicklers
  with PrimitiveArrayPicklers
  with OptionPicklers
  with ThrowablePicklers
  with JavaExtraPicklers
  with TypeExpressionPicklers
  with Tuple2Picklers
  with RefPicklers
  with LowPriorityCustomPicklers
  with SerializationPicklers {}

trait LowPriorityCustomPicklers extends VectorPicklers
  with ListPicklers
  with ArrayPicklers
  with SeqPicklers
  with StringMapPicklers {}
