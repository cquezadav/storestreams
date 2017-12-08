package consumer

import java.net.InetAddress
import java.util.concurrent.{ExecutorService, Executors}
import java.util.{Collections, Properties, TimeZone}

import cassandra.CassandraUtils
import com.datastax.driver.core.utils.UUIDs
import com.google.gson.Gson
import config.ApplicationConfig
import domain.EventMessage
import org.apache.kafka.clients.consumer.{ConsumerConfig, KafkaConsumer}
import org.joda.time.DateTime

import scala.collection.JavaConversions.iterableAsScalaIterable

class RawDataConsumer {

  val tzone = TimeZone.getTimeZone("UTC")
  TimeZone.setDefault(tzone)
  val host = ApplicationConfig.KafkaConfig.host
  val port = ApplicationConfig.KafkaConfig.port
  val topic = ApplicationConfig.KafkaConfig.topic

  val props = createConsumerConfig(s"$host:$port", "group_1")
  val consumer = new KafkaConsumer[String, String](props)
  val cassandraNodes = ApplicationConfig.CassandraConfig.nodes
  val cassandraInets = cassandraNodes.map(InetAddress.getByName).toList
  val cassandraPort = ApplicationConfig.CassandraConfig.port
  var executor: ExecutorService = null
  CassandraUtils.connect(cassandraInets, cassandraPort)

  def shutdown() = {
    if (consumer != null)
      consumer.close();
    if (executor != null)
      executor.shutdown();
  }

  def createConsumerConfig(brokers: String, groupId: String): Properties = {
    val props = new Properties()
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, brokers)
    props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId)
    props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true")
    props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000")
    props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "30000")
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer")
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer")
    props
  }

  def run() = {
    consumer.subscribe(Collections.singletonList(this.topic))

    Executors.newSingleThreadExecutor.execute(new Runnable {
      override def run(): Unit = {
        while (true) {
          val records = consumer.poll(1000)
          val gson = new Gson
          for (record <- records) {
            System.out.println("Received message: (" + record.key() + ", " + record.value() + ") at offset " + record.offset())
            val webMessage = gson.fromJson(record.value(), classOf[EventMessage])
            println(webMessage)

            var messageDate = new DateTime(webMessage.timestamp).toDateTime()
            var year = messageDate.toString("yyyy").toInt
            var month = messageDate.toString("MM").toInt
            var day = messageDate.toString("dd").toInt
            var hour = messageDate.toString("HH").toInt
            var minutes = messageDate.toString("mm").toInt
            println(s"$year - $month - $day - $hour - $minutes")

            DatabaseOperations.insertRawWebEvent(year, month, day, hour, minutes, UUIDs.timeBased(), webMessage.messageId, webMessage.timestamp,
              webMessage.visitOrigin, webMessage.deviceType, webMessage.os, webMessage.location, webMessage.department, webMessage.productId, webMessage.quantity,
              webMessage.action, webMessage.transactionId, webMessage.paymentType, webMessage.shipmentType)
          }
        }
      }
    })
  }
}

object RawDataConsumer extends App {
  new RawDataConsumer().run()
}
