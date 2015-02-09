package endpointng

import java.net.InetSocketAddress

import akka.actor.ActorSystem
import akka.stream.FlowMaterializer
import akka.stream.scaladsl.{StreamTcp, Sink, Source}

object TcpSender {
  def main(args: Array[String]): Unit = {


    implicit val system = ActorSystem("TcpSender")
    implicit val materializer = FlowMaterializer()

    val serverAddress = new InetSocketAddress("127.0.0.1", 6000)

    val testInput = Seq(Person("Issac Assimov", 56), Person("Arthur C. Clark", 52), Person("Robert Heinlein", 47)).toIterator

    val source = Source(() => testInput).transform(() => new PersonSerializer)

    val sink = Sink.ignore

    val outgoingConnection = StreamTcp().outgoingConnection(serverAddress)

    source.via(outgoingConnection.flow).runWith(sink)
  }
}
