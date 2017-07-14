package storestreams.producer

import java.util.Properties

import com.google.gson.GsonBuilder
import org.apache.kafka.clients.producer.{KafkaProducer, Producer, ProducerConfig, ProducerRecord}
import storestreams.config.ApplicationSettings

import scala.util.Random

object EventProducer extends App {

  val props = new Properties()
  val host = sys.env.get("DOCKERHOST").getOrElse(ApplicationSettings.KafkaConfig.kafkaHost)
  val port = ApplicationSettings.KafkaConfig.kafkaPort
  val topic = ApplicationSettings.KafkaConfig.kafkaTopic

  props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, s"$host:$port")
  props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer")
  props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer")
  props.put(ProducerConfig.ACKS_CONFIG, "all")
  props.put(ProducerConfig.CLIENT_ID_CONFIG, "storestreamsproducer")

  val kafkaProducer: Producer[Nothing, String] = new KafkaProducer[Nothing, String](props)
  println(kafkaProducer.partitionsFor(topic))

  val random = new Random()
  while (true) {

    var message = EventGenerator.generate
    val gson = new GsonBuilder().disableHtmlEscaping().create()
    val messageJson = gson.toJson(message)
    println(messageJson)
    val producerRecord = new ProducerRecord(topic, messageJson)
    kafkaProducer.send(producerRecord)
    //Thread.sleep(random.nextInt(50))
    Thread.sleep(500)
  }
}