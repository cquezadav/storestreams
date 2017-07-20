package storestreams.producer

import com.datastax.driver.core.utils.UUIDs
import storestreams.domain.EventMessage
import storestreams.producer.Constants._

import scala.util.Random

object MessageGenerator {

  var counter: Long = 0;

  def generate: EventMessage = {
    val random = new Random()
    val timestamp = System.currentTimeMillis()
    val deviceType = DeviceType(random.nextInt(DeviceType.size))
    val os = if (deviceType.equals("tablet") || deviceType.equals("phone"))
      MobileOperatingSystem(random.nextInt(MobileOperatingSystem.size)) else
      OperatingSystem(random.nextInt(OperatingSystem.size))
    val visitOrigin = VisitOrigin(random.nextInt(VisitOrigin.size))
    val location = UserLocations(random.nextInt(UserLocations.size))
    val department = Department(random.nextInt(Department.size))
    val departmentId = department._1
    val productId = departmentId + random.nextInt(4998)
    val action = Action(random.nextInt(Action.size))
    val transactionId = UUIDs.timeBased().timestamp()
    val quantity = if (action.equals("click") || action.equals("save for later")) 0 else random.nextInt(49) + 1
    val paymentType = if (action.equals("click") || action.equals("save for later")) "none" else PaymentType(random.nextInt(PaymentType.size))
    val shipmentType = if (action.equals("click") || action.equals("save for later")) "none" else ShipmentType(random.nextInt(ShipmentType.size))
    counter += 1
    EventMessage(counter, timestamp, visitOrigin, deviceType, os, location, department._2, productId, quantity, action, transactionId, paymentType, shipmentType)
  }
}
