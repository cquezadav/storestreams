package storestreams.streaming

import org.apache.spark.sql.functions._
import org.apache.spark.sql.{DataFrame, SaveMode}
import storestreams.utils.SparkUtils
import storestreams.utils.config.InitializeApplication

object BatchLayer extends App {

  InitializeApplication.connectSpark()
  val spark = SparkUtils.getOrCreateSparkSession()

  val rawEvents = spark
    .read
    .format("org.apache.spark.sql.cassandra")
    .options(Map("keyspace" -> "storestreams", "table" -> "raw_events"))
    .load()
  val rawEventsCount = rawEvents.groupBy("year", "month", "day", "hour", "location").agg(sum("quantity").as("count"))
  saveToCassandra(rawEventsCount)

  private def saveToCassandra(event: DataFrame) {
    event
      .write.format("org.apache.spark.sql.cassandra")
      .options(Map("table" -> "events_per_location_per_hour_batch", "keyspace" -> "storestreams"))
      .mode(SaveMode.Append).save()
  }
}
