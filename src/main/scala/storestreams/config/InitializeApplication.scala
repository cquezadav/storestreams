package storestreams.config

import java.net.InetAddress

import scala.collection.JavaConversions.asScalaBuffer

import storestreams.utils.CassandraUtils
import storestreams.utils.SparkUtils
import org.apache.spark.streaming.Milliseconds

object InitializeApplication {

  private val cassandraNodes = ApplicationSettings.CassandraConfig.cassandraNodes
  private val cassandraInets = cassandraNodes.map(InetAddress.getByName).toList
  private val cassandraPort = 9042

  def connectCassandra() = {
    CassandraUtils.connect(cassandraInets, cassandraPort)
  }

  def connectSpark() = {
    val applicationName = ApplicationSettings.ApplicationConfig.applicationName
    val sparkHost = ApplicationSettings.SparkConfig.sparkHost
    val sparkPort = ApplicationSettings.SparkConfig.sparkPort
    val cassandraHost = cassandraNodes.get(0)
    SparkUtils.createSparkSession(applicationName, sparkHost, sparkPort, cassandraHost)
  }
  
  def connectStreaming() {
    val applicationName = ApplicationSettings.ApplicationConfig.applicationName
    val sparkHost = ApplicationSettings.SparkConfig.sparkHost
    val sparkPort = ApplicationSettings.SparkConfig.sparkPort
    val cassandraHost = cassandraNodes.get(0)
    SparkUtils.createSparkStreaming(applicationName, sparkHost, sparkPort, cassandraHost, Milliseconds(500))
  }

}