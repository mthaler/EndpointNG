package endpointng

import akka.stream.scaladsl.{Sink, Source}

trait Endpoint[S, R] {

  def source: Source[S]

  def sink: Sink[R]
}
