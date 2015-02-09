package endpointng

import akka.actor.ActorSystem
import akka.stream.scaladsl.StreamTcp.OutgoingConnection
import akka.stream.scaladsl.{Source, Sink}
import akka.stream.stage.Stage
import akka.util.ByteString

class TcpEndpoint(system: ActorSystem,connection: OutgoingConnection) extends AbstractEndpoint[Person, Ping](system) {

  override def serializer: Stage[Person, ByteString] = new PersonSerializer

  override def deserializer: Stage[ByteString, Ping] = new PingDeserializer

  val testInput = Seq(Person("Issac Assimov", 56), Person("Arthur C. Clark", 52), Person("Robert Heinlein", 47)).toIterator

  override val source: Source[Person] = Source(() => testInput)

  override val sink = Sink.foreach(println)

  source.transform(() => serializer).via(connection.flow.transform(() => deserializer)).runWith(sink)
}
