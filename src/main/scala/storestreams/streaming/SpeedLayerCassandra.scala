package storestreams.streaming

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.functions._
import org.apache.spark.sql.{DataFrame, SaveMode}
import org.apache.spark.streaming._
import org.apache.spark.streaming.kafka010.{ConsumerStrategies, KafkaUtils, LocationStrategies}
import org.joda.time.DateTime
import play.api.libs.json.Json
import storestreams.domain.{EventMessage, EventPerLocationPerHourCount, EventTimeLocation}
import storestreams.utils.SparkUtils
import storestreams.utils.config.{ApplicationConfig, InitializeApplication}

import scala.collection.mutable

object SpeedLayerCassandra extends App {

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
  kafkaParam.put(ConsumerConfig.GROUP_ID_CONFIG, "speed_group_1")
  kafkaParam.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest")
  kafkaParam.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true")
  val topicList = List(topic)

  // Read value of each message from Kafka and return it
  val rawEventMessagesStream = KafkaUtils.createDirectStream(streamingContext,
    LocationStrategies.PreferConsistent,
    ConsumerStrategies.Subscribe[String, String](topicList, kafkaParam))
  val rawEvents = rawEventMessagesStream.map(consumerRecord => deserializeEvent(consumerRecord.value()))

  val mappingFunc = (key: EventTimeLocation, value: Option[Long], state: State[Long]) => {
    val sum = value.getOrElse(0L) + state.getOption().getOrElse(0L)
    println(s"$key -> value: $value; state: $state; updated: $sum")
    state.update(sum)
    (key, sum)
  }
  val updatedEventState = rawEvents.mapWithState(StateSpec.function(mappingFunc))

  updatedEventState.foreachRDD { event =>
    val eventsPerLocationPerHourDF = eventPerLocationPerHourCountRdd(event)
    saveToCassandra(eventsPerLocationPerHourDF, "storestreams", "events_per_location_per_hour_speed")

    val eventsPerLocationPerDayDF = eventsPerLocationPerHourDF.groupBy("year", "month", "day", "location").agg(sum("count").as("count"))
    saveToCassandra(eventsPerLocationPerDayDF, "storestreams", "events_per_location_per_day_speed")

    val eventsPerLocationPerMonthDF = eventsPerLocationPerDayDF.groupBy("year", "month", "location").agg(sum("count").as("count"))
    saveToCassandra(eventsPerLocationPerMonthDF, "storestreams", "events_per_location_per_month_speed")

    val eventsPerLocationPerYearDF = eventsPerLocationPerMonthDF.groupBy("year", "location").agg(sum("count").as("count"))
    saveToCassandra(eventsPerLocationPerYearDF, "storestreams", "events_per_location_per_year_speed")

    val eventsPerLocationDF = eventsPerLocationPerYearDF.groupBy("location").agg(sum("count").as("count"))
    saveToCassandra(eventsPerLocationDF, "storestreams", "events_per_location_speed")

    val eventsPerYearDF = eventsPerLocationPerYearDF.groupBy("year").agg(sum("count").as("count"))
    saveToCassandra(eventsPerYearDF, "storestreams", "events_per_year_speed")

    val eventsPerMonthDF = eventsPerLocationPerMonthDF.groupBy("year", "month").agg(sum("count").as("count"))
    saveToCassandra(eventsPerMonthDF, "storestreams", "events_per_month_speed")

    val eventsPerDayhDF = eventsPerLocationPerDayDF.groupBy("year", "month", "day").agg(sum("count").as("count"))
    saveToCassandra(eventsPerDayhDF, "storestreams", "events_per_day_speed")

    val eventsPerHourDF = eventsPerLocationPerHourDF.groupBy("year", "month", "day", "hour").agg(sum("count").as("count"))
    saveToCassandra(eventsPerHourDF, "storestreams", "events_per_hour_speed")
  }

  streamingContext.start()
  streamingContext.awaitTermination()

  def eventPerLocationPerHourCountRdd(event: RDD[(EventTimeLocation, Long)]) = {
    spark.createDataFrame(event.map(e =>
      EventPerLocationPerHourCount(e._1.year, e._1.month, e._1.day, e._1.hour, e._1.location, Some(e._2))))
  }

  private def deserializeEvent(input: String): (EventTimeLocation, Long) = {
    val event = Json.parse(input).as[EventMessage]
    var messageDate = new DateTime(event.timestamp).toDateTime()
    var year = messageDate.toString("yyyy")
    var month = messageDate.toString("MM")
    var day = messageDate.toString("dd")
    var hour = messageDate.toString("HH")
    //var minutes = messageDate.toString("mm")
    val location = event.location
    //var id = s"$year|$month|$day|$hour|$location"
    val quantity = event.quantity
    (EventTimeLocation(year.toInt, Some(month.toInt), Some(day.toInt), Some(hour.toInt), location), quantity)
  }

  private def saveToCassandra(event: DataFrame, keyspace: String, table: String) {
    event
      .write.format("org.apache.spark.sql.cassandra")
      .options(Map("keyspace" -> keyspace, "table" -> table))
      .mode(SaveMode.Append).save()
  }
}
