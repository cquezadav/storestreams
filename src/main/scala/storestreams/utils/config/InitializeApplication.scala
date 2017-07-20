package storestreams.utils.config

import java.net.InetAddress

import storestreams.utils.{CassandraUtils, SparkUtils}

import scala.collection.JavaConversions.asScalaBuffer

/**
  * Initializes application services
  */
object InitializeApplication {

  def connectCassandra() = {
    val cassandraNodes = ApplicationConfig.CassandraConfig.nodes
    val cassandraInets = cassandraNodes.map(InetAddress.getByName).toList
    val cassandraPort = ApplicationConfig.CassandraConfig.port
    CassandraUtils.connect(cassandraInets, cassandraPort)
  }

  def connectSpark() = {
    SparkUtils.getOrCreateSparkSession()
  }

  def connectStreaming() {
    SparkUtils.getOrCreateStreamingContext()
  }
}