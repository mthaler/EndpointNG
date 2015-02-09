package endpointng

import java.io.{ObjectOutputStream, ByteArrayOutputStream}

class PersonSerializer extends AbstractSerializer[Person] {

  override def serialize(elem: Person): Array[Byte] = {
    val bout = new ByteArrayOutputStream()
    val out = new ObjectOutputStream(bout)
    out.writeObject(elem)
    val bytes = bout.toByteArray
    out.close()
    bout.close()
    bytes
  }
}
