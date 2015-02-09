package endpointng

import akka.actor.ActorSystem
import akka.stream.scaladsl.{Sink, Source}
import akka.stream.scaladsl.StreamTcp.IncomingConnection
import akka.stream.stage.Stage
import akka.util.ByteString
import scala.concurrent.duration._
import scala.language.postfixOps

class TcpReceiverEndpoint(system: ActorSystem,connection: IncomingConnection) extends AbstractEndpoint[Ping, Person](system) {

  override def serializer: Stage[Ping, ByteString] = new PingSerializer

  override def deserializer: Stage[ByteString, Person] = new PersonDeserializer

  override val source = Source.apply(1 second, 1 second, () => new Ping)

  override val sink = Sink.foreach(println)

  source.transform(() => serializer).via(connection.flow.transform(() => deserializer)).runWith(sink)
}
