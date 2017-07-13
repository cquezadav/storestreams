package storestreams.domain

case class EventPerLocation(year: Int, month: Option[Int] = None, day: Option[Int] = None, hour: Option[Int] = None,
                             location: String, count: Option[Long] = None) extends Serializable

