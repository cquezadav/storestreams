package storestreams.utils

import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession
import org.apache.spark.streaming.{Seconds, StreamingContext}
import storestreams.utils.config.ApplicationConfig

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

    // TODO
    //val checkpointDirectory = "//checkpoint"
    //sparkSession.sparkContext.setCheckpointDir(checkpointDirectory)
  }

  def getOrCreateStreamingContext() = {
    if (streamingContext == null)
      createStreamingContext()
      streamingContext
  }

  private def createStreamingContext() = {
    val batchDuration = ApplicationConfig.SparkStreamingConfig.batchDuration
    val checkpoint = ApplicationConfig.SparkStreamingConfig.checkpoint
    streamingContext = new StreamingContext(sparkSession.sparkContext, Seconds(batchDuration))
    streamingContext.checkpoint(checkpoint)
  }

  // TODO
  /*def getStreamingContext(streamingApp: (SparkContext, Duration) => StreamingContext, sc: SparkContext, batchDuration: Duration) = {
    val creatingFunc = () => streamingApp(sc, batchDuration)
    val ssc = sc.getCheckpointDir match {
      case Some(checkpointDir) => StreamingContext.getActiveOrCreate(checkpointDir, creatingFunc, sc.hadoopConfiguration, createOnError = true)
      case None                => StreamingContext.getActiveOrCreate(creatingFunc)
    }
    sc.getCheckpointDir.foreach(cp => ssc.checkpoint(cp))
    ssc
  }*/
}