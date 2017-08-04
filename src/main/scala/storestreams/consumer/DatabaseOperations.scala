package storestreams.consumer

import java.util.UUID

import com.datastax.driver.core.exceptions.DriverException
import storestreams.utils.CassandraUtils

object DatabaseOperations extends App {

  def insertRawWebEvent(year: Int, month: Int, day: Int, hour: Int, minutes: Int, eventId: UUID, messageId: Long,
                        timestamp: Long, visitOrigin: String, deviceType: String, os: String, location: String,
                        department: String, productId: Long, quantity: Int, action: String, transactionId: Long,
                        paymentType: String, shipmentType: String) = {

    var cql =
      s"""INSERT INTO storestreams.raw_events
      (year, month, day, hour, minutes, event_id, message_id, timestamp, visit_origin, device_type, os, location,
      department, product_id, quantity, action, transaction_id, payment_type, shipment_type)
      VALUES ($year, $month, $day, $hour, $minutes, $eventId, $messageId, $timestamp, '$visitOrigin', '$deviceType',
              '$os', '$location', '$department', $productId, $quantity, '$action', $transactionId, '$paymentType',
              '$shipmentType')"""

    println(cql)
    val session = CassandraUtils.getSession()
    try {
      session.execute(cql)
    } catch {
      case e: DriverException => println("Could not insert web event into cassandra: " + e)
      case e: Exception => println("Got this unknown exception: " + e)
    }
  }
}
