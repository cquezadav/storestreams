package domain

import play.api.libs.json._

case class EventMessage(messageId: Long, timestamp: Long, visitOrigin: String, deviceType: String,
                        os: String, location: String, department: String, productId: Long,
                        quantity: Int, action: String, transactionId: Long, paymentType: String,
                        shipmentType: String) extends Serializable

//case class Event(messageId: Long, name: String) extends Serializable

object EventMessage {
  implicit val eventMessageReads = Json.reads[EventMessage]
  implicit val eventMessageWrites = Json.writes[EventMessage]
}