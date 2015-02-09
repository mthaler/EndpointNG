package endpointng

import java.io.{ObjectOutputStream, ByteArrayOutputStream}

class PingSerializer extends AbstractSerializer[Ping] {

  override def serialize(elem: Ping): Array[Byte] = {
    val bout = new ByteArrayOutputStream()
    val out = new ObjectOutputStream(bout)
    out.writeObject(elem)
    val bytes = bout.toByteArray
    out.close()
    bout.close()
    bytes
  }
}
