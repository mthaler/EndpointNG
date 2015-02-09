package endpointng

import java.io.ObjectInputStream

import akka.util.ByteString

class PersonDeserializer extends AbstractDeserializer[Person] {

  override protected def parse(bytes: ByteString): Person = {
    val bin = bytes.iterator.asInputStream
    val in = new ObjectInputStream(bin)
    in.readObject.asInstanceOf[Person]
  }
}

