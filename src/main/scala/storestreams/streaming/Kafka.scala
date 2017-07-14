package storestreams.streaming

import java.util.Properties

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.types.{IntegerType, StringType, StructType}
import org.apache.spark.streaming.kafka010.{ConsumerStrategies, KafkaUtils, LocationStrategies}
import org.apache.spark.streaming.{Durations, StreamingContext}
import org.joda.time.DateTime
import storestreams.config.ApplicationSettings
import storestreams.domain.EventPerLocation

import scala.collection.mutable

object Kafka extends App {

  val props = new Properties()
  val host = sys.env.get("DOCKERHOST").getOrElse(ApplicationSettings.KafkaConfig.kafkaHost)
  val port = ApplicationSettings.KafkaConfig.kafkaPort
  val topic = ApplicationSettings.KafkaConfig.kafkaTopic
  val spark = SparkSession.builder().master("local[*]").getOrCreate()

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

  val conf = new SparkConf().setMaster("local[2]").setAppName("Kafka10")

  //Read messages in batch of 30 seconds
  val sparkStreamingContext = new StreamingContext(spark.sparkContext, Durations.seconds(5))

  //Configure Spark to listen messages in topic test
  val topicList = List(topic)

  val schema = (new StructType)
    .add("messageId", IntegerType)
    .add("name", StringType)

  // Read value of each message from Kafka and return it
  val messageStream = KafkaUtils.createDirectStream(sparkStreamingContext,
    LocationStrategies.PreferConsistent,
    ConsumerStrategies.Subscribe[String, String](topicList, kafkaParam))
  val lines = messageStream.map(consumerRecord => consumerRecord.value().asInstanceOf[String])

  import spark.implicits._
  val events = lines.foreachRDD { e =>
    toEventsPerLocationDF(e).map (x => (x.getInt(0).toString + x.getInt(1).toString + x.getInt(2).toString + x.getInt(3).toString + x.getString(4), x.getLong(5)))
  }

  //val wordCounts = splits.map(x => (x, 1)).reduceByKey(_+_).updateStateByKey(updateFunc)


  def toEventsPerLocationDF(input: RDD[String]) = {
    spark.createDataFrame(
      spark.read.json(input).rdd.map { x =>
        var messageDate = new DateTime(x.getAs[Long]("timestamp")).toDateTime()
        var year = messageDate.toString("yyyy").toInt
        var month = messageDate.toString("MM").toInt
        var day = messageDate.toString("dd").toInt
        var hour = messageDate.toString("HH").toInt
        var minutes = messageDate.toString("mm").toInt
        val location = x.getAs[String]("location")
        EventPerLocation(year, Some(month), Some(day), Some(hour), location)
      }).groupBy("year", "month", "day", "hour", "location").count()
  }

  val updateFunc = (values: Seq[Int], state: Option[Int]) => {
    val currentCount = values.foldLeft(0)(_ + _)

    val previousCount = state.getOrElse(0)

    Some(currentCount + previousCount)
  }

  sparkStreamingContext.checkpoint("checkpoint")
  //  val rdd = lines.foreachRDD { e =>
  //    val event = spark.read.schema(schema).json(e)
  //    event.write.partitionBy("name").format("parquet").mode(SaveMode.Append).save("events")


  // Break every message into words and return list of words
  //val words = lines.flatMap(_.split(" "))
  // Take every word and return Tuple with (word,1)
  //val wordMap = words.map(word => (word, 1))
  // Count occurance of each word
  //val wordCount = wordMap.reduceByKey((first, second) => first + second)

  //Print the word count
  //lines.print()

  sparkStreamingContext.start()
  sparkStreamingContext.awaitTermination()

}
