package storestreams.producer

import storestreams.domain.Event

object EventGenerator {

  var counter: Long = 0;

  def generate: Event = {
    counter += 1
    Event(counter, "event")
  }
}
