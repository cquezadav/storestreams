package producer

import java.util.{Properties, TimeZone}

import com.google.gson.GsonBuilder
import config.ApplicationConfig
import org.apache.kafka.clients.producer.{KafkaProducer, Producer, ProducerConfig, ProducerRecord}

import scala.util.Random

object MessageProducer extends App {

  val tzone = TimeZone.getTimeZone("UTC")
  TimeZone.setDefault(tzone)
  val props = new Properties()
  val host = ApplicationConfig.KafkaConfig.host
  val port = ApplicationConfig.KafkaConfig.port
  val topic = ApplicationConfig.KafkaConfig.topic
  val timeWindow = ApplicationConfig.MessagesProducer.timeWindow

  props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, s"$host:$port")
  props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer")
  props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer")
  props.put(ProducerConfig.ACKS_CONFIG, "all")
  props.put(ProducerConfig.CLIENT_ID_CONFIG, "storestreamsproducer")

  println(props)
  val kafkaProducer: Producer[Nothing, String] = new KafkaProducer[Nothing, String](props)
  println(kafkaProducer.partitionsFor(topic))

  val random = new Random()
  while (true) {

    var message = MessageGenerator.generate
    val gson = new GsonBuilder().disableHtmlEscaping().create()
    val messageJson = gson.toJson(message)
    println(messageJson)
    val producerRecord = new ProducerRecord(topic, messageJson)
    kafkaProducer.send(producerRecord)
    //Thread.sleep(random.nextInt(50))
    Thread.sleep(timeWindow)
  }
}