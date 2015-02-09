package endpointng

import java.io.ObjectInputStream

import akka.util.ByteString

class PingDeserializer extends AbstractDeserializer[Ping] {

  override protected def parse(bytes: ByteString): Ping = {
    val bin = bytes.iterator.asInputStream
    val in = new ObjectInputStream(bin)
    in.readObject.asInstanceOf[Ping]
  }
}
