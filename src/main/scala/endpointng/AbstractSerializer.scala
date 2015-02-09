package endpointng

import java.nio.ByteOrder

import akka.stream.stage.{Directive, Context, PushStage}
import akka.util.{ByteString, ByteStringBuilder}

abstract class AbstractSerializer[S] extends PushStage[S, ByteString] {

  final override def onPush(elem: S, ctx: Context[ByteString]): Directive = {
    val bytes = serialize(elem)
    val b = new ByteStringBuilder
    b.putInt(bytes.size)(ByteOrder.LITTLE_ENDIAN)
    b ++= bytes
    ctx.push(b.result())
  }

  protected def serialize(elem: S): Array[Byte]
}
