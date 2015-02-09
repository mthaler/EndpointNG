package endpointng

import java.net.InetSocketAddress

import akka.actor.ActorSystem
import akka.stream.FlowMaterializer
import akka.stream.scaladsl.{StreamTcp, ForeachSink}
import scala.language.postfixOps

object TcpReceiver {

  def main(args: Array[String]): Unit = {
    implicit val system = ActorSystem("TcpReceiver")
    implicit val materializer = FlowMaterializer()

    val handler = ForeachSink[StreamTcp.IncomingConnection] { conn =>
      println("Client connected from: " + conn.remoteAddress)

      new TcpReceiverEndpoint(system, conn)
    }

    val serverAddress = new InetSocketAddress("127.0.0.1", 6000)
    val binding = StreamTcp().bind(serverAddress)
    val materializedServer = binding.connections.to(handler).run()
  }
}
