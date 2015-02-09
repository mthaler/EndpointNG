package endpointng

import java.net.InetSocketAddress

import akka.actor.ActorSystem
import akka.stream.FlowMaterializer
import akka.stream.scaladsl.StreamTcp

object TcpSender {
  def main(args: Array[String]): Unit = {


    implicit val system = ActorSystem("TcpSender")
    implicit val materializer = FlowMaterializer()

    val serverAddress = new InetSocketAddress("127.0.0.1", 6000)
    val connection = StreamTcp().outgoingConnection(serverAddress)

    val endpoint = new TcpEndpoint(system, connection)
  }
}
