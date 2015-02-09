package endpointng

import java.io.{ObjectOutputStream, ByteArrayOutputStream}
import java.nio.ByteOrder

import akka.stream.stage.{Directive, Context, PushStage}
import akka.util.{ByteStringBuilder, ByteString}

class PersonSerializer extends PushStage[Person, ByteString] {

  override def onPush(elem: Person, ctx: Context[ByteString]): Directive = {
    val bout = new ByteArrayOutputStream()
    val out = new ObjectOutputStream(bout)
    out.writeObject(elem)
    val bytes = bout.toByteArray
    out.close()
    bout.close()
    val b = new ByteStringBuilder
    b.putInt(bytes.size)(ByteOrder.LITTLE_ENDIAN)
    b.append(ByteString(bytes))
    ctx.push(b.result())
  }
}
