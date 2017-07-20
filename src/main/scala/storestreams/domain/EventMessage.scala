package storestreams.domain

import play.api.libs.json._

case class EventMessage(messageId: Long, timestamp: Long, visitOrigin: String, deviceType: String,
                        os: String, location: String, department: String, productId: Long,
                        quantity: Int, action: String, transactionId: Long, paymentType: String,
                        shipmentType: String) extends Serializable

case class Event(messageId: Long, name: String) extends Serializable

object EventMessage {

  implicit val eventMessageReads = Json.reads[EventMessage]
  implicit val eventMessageWrites = Json.writes[EventMessage]
}

object event extends App {
  val message = "{\"messageId\":1,\"timestamp\":1500183579886,\"visitOrigin\":\"email_ad\",\"deviceType\":\"mac\",\"os\":\"chrome\",\"location\":\"VT\",\"department\":\"Software\",\"productId\":90059,\"quantity\":18,\"action\":\"add_to_cart\",\"transactionId\":137194763801180000,\"paymentType\":\"paypal\",\"shipmentType\":\"shipment\"}";
  val event = Json.parse(message).as[EventMessage]
  println(event)
}