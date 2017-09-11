package storestreams.utils.config

import com.typesafe.config.ConfigFactory

/**
  * Gets configuration from application.conf file
  */
object ApplicationConfig {
  private val config = ConfigFactory.load()
  private val rootConfig = config.getConfig("storestreams")

  object ApplicationConfig {
    lazy val applicationName = rootConfig.getString("applicationName")
  }

  object CassandraConfig {
    private val cassandraConfig = rootConfig.getConfig("cassandra")
    lazy val nodes = cassandraConfig.getStringList("nodes")
    lazy val port = cassandraConfig.getInt("port")
  }

  object SchemaConfig {
    private val schemaConfig = rootConfig.getConfig("schema")
    lazy val keyspace = schemaConfig.getString("keyspace")
    lazy val rawEventsTable = schemaConfig.getString("rawEventsTable")
    lazy val eventsPerLocationPerHourBatchTable = schemaConfig.getString("eventsPerLocationPerHourBatchTable")
    lazy val eventsPerLocationPerHourSpeedTable = schemaConfig.getString("eventsPerLocationPerHourSpeedTable")
    lazy val eventsPerLocationPerDayBatchTable = schemaConfig.getString("eventsPerLocationPerDayBatchTable")
    lazy val eventsPerLocationPerMonthBatchTable = schemaConfig.getString("eventsPerLocationPerMonthBatchTable")
    lazy val eventsPerLocationPerYearBatchTable = schemaConfig.getString("eventsPerLocationPerYearBatchTable")
  }

  object KafkaConfig {
    private val kafkaConfig = rootConfig.getConfig("kafka")
    lazy val host = kafkaConfig.getString("host")
    lazy val port = kafkaConfig.getString("port")
    lazy val topic = kafkaConfig.getString("topic")
  }

  object SparkConfig {
    private val sparkConfig = rootConfig.getConfig("spark")
    lazy val host = sparkConfig.getString("host")
    lazy val port = sparkConfig.getString("port")
  }

  object SparkStreamingConfig {
    private val sparkStreamingConfig = rootConfig.getConfig("sparkStreaming")
    lazy val batchDuration = sparkStreamingConfig.getInt("batchDuration")
    lazy val checkpoint = sparkStreamingConfig.getString("checkpoint")
  }

  object MessagesProducer {
    private val messagesProducerConfig = rootConfig.getConfig("messagesProducer")
    lazy val timeWindow = messagesProducerConfig.getInt("timeWindow")
  }

}
