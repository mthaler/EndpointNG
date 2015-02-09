package endpointng

import akka.actor.ActorSystem
import akka.stream.FlowMaterializer
import akka.stream.stage.Stage
import akka.util.ByteString

abstract class AbstractEndpoint[S, R](system: ActorSystem) extends Endpoint[S, R] {

  implicit val sys = system
  implicit val materializer = FlowMaterializer()

  def serializer: Stage[S, ByteString]

  def deserializer: Stage[ByteString, R]
}
