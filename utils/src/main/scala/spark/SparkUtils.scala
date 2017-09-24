package spark

import config.ApplicationConfig
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession
import org.apache.spark.streaming.{Seconds, StreamingContext}

object SparkUtils {

  private var sparkConf: SparkConf = null
  private var sparkSession: SparkSession = null
  private var streamingContext: StreamingContext = null;

  def getOrCreateSparkSession() = {
    if (sparkSession == null)
      createSparkSession()
    sparkSession
  }

  private def createSparkSession() = {
    val appName = ApplicationConfig.ApplicationConfig.applicationName
    val host = ApplicationConfig.SparkConfig.host
    val port = ApplicationConfig.SparkConfig.port
    val cassandraHost = ApplicationConfig.CassandraConfig.nodes.get(0)
    val sparkHost = if (!port.isEmpty) s"spark://$host:$port" else host
    sparkConf = new SparkConf(true)
      .setAppName(appName)
      .setMaster(sparkHost)
      .set("spark.cassandra.connection.host", cassandraHost)
    sparkSession = SparkSession.builder().config(sparkConf).getOrCreate()
  }

  def getOrCreateStreamingContext(withCheckpoint: Boolean) = {
    if (streamingContext == null)
      createStreamingContext(withCheckpoint)
    streamingContext
  }

  private def createStreamingContext(withCheckpoint: Boolean) = {
    val batchDuration = ApplicationConfig.SparkStreamingConfig.batchDuration
    streamingContext = new StreamingContext(sparkSession.sparkContext, Seconds(batchDuration))
    if (withCheckpoint) {
      val checkpoint = ApplicationConfig.SparkStreamingConfig.checkpoint
      streamingContext.checkpoint(checkpoint)
    }
  }
}