package storestreams.domain

case class EventPerLocationPerHourCount(var year: Int = 0, var month: Option[Int] = None, var day: Option[Int] = None, var hour: Option[Int] = None,
                            var location: String = "NA", var count: Option[Long] = None) extends Serializable

case class EventTimeLocation(var year: Int = 0, var month: Option[Int] = None, var day: Option[Int] = None, var hour: Option[Int] = None,
                            var location: String = "NA") extends Serializable
