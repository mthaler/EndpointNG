package endpointng

import java.io.ObjectInputStream
import java.nio.ByteOrder

import akka.stream.stage.{Directive, Context, StatefulStage}
import akka.util.ByteString

import scala.annotation.tailrec

class PersonDeserializer extends StatefulStage[ByteString, Person] {

  private val lengthPrefixSize = 4
  private var buffer = ByteString.empty
  private var length: Option[Int] = None

  def initial = new State {

    override def onPush(chunk: ByteString, ctx: Context[Person]): Directive = {
      buffer ++= chunk
      emit(doParse(Vector.empty).iterator, ctx)
    }
  }

  @tailrec
  private def doParse(parsedItems: Vector[Person]): Vector[Person] = {
    if (length.isEmpty) {
      if (buffer.size >= lengthPrefixSize) {
        val l  = buffer.take(lengthPrefixSize).iterator.getInt(ByteOrder.LITTLE_ENDIAN)
        length = Some(l)
        buffer = buffer.drop(lengthPrefixSize)
      }
    }
    length match {
      case Some(l) =>
        if (buffer.size >= l) {
          val bin = buffer.take(l).iterator.asInputStream
          buffer = buffer.drop(l)
          length = None
          val in = new ObjectInputStream(bin)
          val item = in.readObject.asInstanceOf[Person]
          doParse(parsedItems :+ item)
        } else {
          parsedItems
        }
      case None => parsedItems
    }
  }
}

