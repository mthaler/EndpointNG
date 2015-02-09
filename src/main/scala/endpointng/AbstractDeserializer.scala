package endpointng

import java.nio.ByteOrder

import akka.stream.stage.{Directive, Context, StatefulStage}
import akka.util.ByteString

import scala.annotation.tailrec

abstract class AbstractDeserializer[R] extends StatefulStage[ByteString, R] {

  private val lengthPrefixSize = 4
  private var buffer = ByteString.empty
  private var length: Option[Int] = None

  final def initial = new State {

    override def onPush(chunk: ByteString, ctx: Context[R]): Directive = {
      buffer ++= chunk
      emit(doParse(Vector.empty).iterator, ctx)
    }
  }

  @tailrec
  private def doParse(parsedItems: Vector[R]): Vector[R] = {
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
          val bytes = buffer.take(l)
          buffer = buffer.drop(l)
          length = None
          val item = parse(bytes)
          doParse(parsedItems :+ item)
        } else {
          parsedItems
        }
      case None => parsedItems
    }
  }

  protected def parse(bytes: ByteString): R
}
