package endpointng

import java.net.InetSocketAddress

import akka.actor.ActorSystem
import akka.stream.FlowMaterializer
import akka.stream.scaladsl.{Sink, Source, StreamTcp, ForeachSink}
import akka.util.ByteString

object TcpReceiver {

  def main(args: Array[String]): Unit = {
    implicit val system = ActorSystem("TcpReceiver")
    implicit val materializer = FlowMaterializer()

    val handler = ForeachSink[StreamTcp.IncomingConnection] { conn =>
      println("Client connected from: " + conn.remoteAddress)

      val source = Source.empty[ByteString]

      val sink = Sink.foreach(println)

      source.via(conn.flow.transform(() => new PersonDeserializer)).runWith(sink)
    }

    val serverAddress = new InetSocketAddress("127.0.0.1", 6000)
    val binding = StreamTcp().bind(serverAddress)
    val materializedServer = binding.connections.to(handler).run()
  }
}
