package storestreams.streaming

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SaveMode
import org.apache.spark.streaming._
import org.apache.spark.streaming.kafka010.{ConsumerStrategies, KafkaUtils, LocationStrategies}
import org.joda.time.DateTime
import play.api.libs.json.Json
import storestreams.domain.{EventMessage, EventPerLocationPerHourCount, EventTimeLocation}
import storestreams.utils.SparkUtils
import storestreams.utils.config.{ApplicationConfig, InitializeApplication}

import scala.collection.mutable

object Kafka extends App {

  InitializeApplication.connectSpark()
  InitializeApplication.connectStreaming()
  val host = ApplicationConfig.KafkaConfig.host
  val port = ApplicationConfig.KafkaConfig.port
  val topic = ApplicationConfig.KafkaConfig.topic
  val spark = SparkUtils.getOrCreateSparkSession()
  val streamingContext = SparkUtils.getOrCreateStreamingContext()

  // Configure Spark to connect to Kafka running on local machine
  val kafkaParam = new mutable.HashMap[String, String]()
  kafkaParam.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, s"$host:$port")
  kafkaParam.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
    "org.apache.kafka.common.serialization.StringDeserializer")
  kafkaParam.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
    "org.apache.kafka.common.serialization.StringDeserializer")
  kafkaParam.put(ConsumerConfig.GROUP_ID_CONFIG, "group1")
  kafkaParam.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest")
  kafkaParam.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true")
  val topicList = List(topic)

  // Read value of each message from Kafka and return it
  val rawEventMessagesStream = KafkaUtils.createDirectStream(streamingContext,
    LocationStrategies.PreferConsistent,
    ConsumerStrategies.Subscribe[String, String](topicList, kafkaParam))
  val rawEvents = rawEventMessagesStream.map(consumerRecord => deserializeEvent(consumerRecord.value()))

  val mappingFunc = (key: String, value: Option[(EventTimeLocation, Long)], state: State[(EventTimeLocation, Long)]) => {
    val valueEventTimeLocation = value.getOrElse(new EventTimeLocation, 0L)._1
    val stateEventTimeLocation = state.getOption().getOrElse(valueEventTimeLocation, 0L)._1
    val sum = value.getOrElse(new EventTimeLocation, 0L)._2 + state.getOption.getOrElse(new EventTimeLocation, 0L)._2
    state.update((stateEventTimeLocation, sum))
    (stateEventTimeLocation, sum)
  }
  val updatedEventState = rawEvents.mapWithState(StateSpec.function(mappingFunc))

  updatedEventState.foreachRDD(x => saveToCassandra(x))

  streamingContext.start()
  streamingContext.awaitTermination()

  private def deserializeEvent(input: String): (String, (EventTimeLocation, Long)) = {
    val event = Json.parse(input).as[EventMessage]
    var messageDate = new DateTime(event.timestamp).toDateTime()
    var year = messageDate.toString("yyyy")
    var month = messageDate.toString("MM")
    var day = messageDate.toString("dd")
    var hour = messageDate.toString("HH")
    var minutes = messageDate.toString("mm")
    val location = event.location
    var id = s"$year|$month|$day|$hour|$location"
    val quantity = event.quantity
    (id, (EventTimeLocation(year.toInt, Some(month.toInt), Some(day.toInt), Some(hour.toInt), location), quantity))
  }

  private def saveToCassandra(event: RDD[(EventTimeLocation, Long)]) {
    spark.createDataFrame(event.map(e =>
      EventPerLocationPerHourCount(e._1.year, e._1.month, e._1.day, e._1.hour, e._1.location, Some(e._2))))
      .write.format("org.apache.spark.sql.cassandra")
      .options(Map("table" -> "events_per_location_per_hour_speed", "keyspace" -> "storestreams"))
      .mode(SaveMode.Append).save()
  }
}
