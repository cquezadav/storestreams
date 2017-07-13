package storestreams.consumer

import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe
import org.apache.spark.streaming.kafka010.KafkaUtils
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent
import storestreams.config.{ApplicationSettings, InitializeApplication}
import storestreams.utils.SparkUtils

object Speed extends App {

  InitializeApplication.connectStreaming()
  InitializeApplication.connectSpark()
  val streaming = SparkUtils.getStreamingContext
  val spark = SparkUtils.getSparkSession

  val kafkaTopic = ApplicationSettings.KafkaConfig.kafkaTopic
  val kafkaHost = ApplicationSettings.KafkaConfig.kafkaHost
  val kafkaPort = ApplicationSettings.KafkaConfig.kafkaPort

  val kafkaParams = Map[String, Object](
    "bootstrap.servers" -> s"$kafkaHost:$kafkaPort",
    "key.deserializer" -> classOf[StringDeserializer],
    "value.deserializer" -> classOf[StringDeserializer],
    "group.id" -> "group_1",
    "auto.offset.reset" -> "latest",
    "max.poll.records" -> "5000",
    "fetch.min.bytes" -> "1000",
    "enable.auto.commit" -> (false: java.lang.Boolean))

  val messages = KafkaUtils.createDirectStream[String, String](streaming, PreferConsistent, Subscribe[String, String](Set(kafkaTopic), kafkaParams))
  messages.print()
  messages.map(record => (record.key, record.value)).count().print()

  streaming.start()
  streaming.awaitTermination()
}

