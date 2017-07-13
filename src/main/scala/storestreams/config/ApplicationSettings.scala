package storestreams.config;

import com.typesafe.config.ConfigFactory

object ApplicationSettings {
  private val config = ConfigFactory.load()
  private val rootConfig = config.getConfig("webEvents")

  object ApplicationConfig {
    lazy val applicationName = rootConfig.getString("applicationName")
  }

  object CassandraConfig {
    private val cassandraConfig = rootConfig.getConfig("cassandra")
    lazy val cassandraNodes = cassandraConfig.getStringList("nodes")
  }

  object SchemaConfig {
    private val schemaConfig = rootConfig.getConfig("schema")
    lazy val keyspace = schemaConfig.getString("keyspace")
    lazy val rawEventsDataTable = schemaConfig.getString("rawEventsDataTable")
    lazy val eventsPerLocationPerHourBatchTable = schemaConfig.getString("eventsPerLocationPerHourBatchTable")
    lazy val eventsPerLocationPerHourSpeedTable = schemaConfig.getString("eventsPerLocationPerHourSpeedTable")
    lazy val eventsPerLocationPerDayBatchTable = schemaConfig.getString("eventsPerLocationPerDayBatchTable")
    lazy val eventsPerLocationPerMonthBatchTable = schemaConfig.getString("eventsPerLocationPerMonthBatchTable")
    lazy val eventsPerLocationPerYearBatchTable = schemaConfig.getString("eventsPerLocationPerYearBatchTable")
  }

  object KafkaConfig {
    private val kafkaConfig = rootConfig.getConfig("kafka")
    lazy val kafkaHost = kafkaConfig.getString("host")
    lazy val kafkaPort = kafkaConfig.getString("port")
    lazy val kafkaTopic = kafkaConfig.getString("topic")
  }

  object SparkConfig {
    private val sparkConfig = rootConfig.getConfig("spark")
    lazy val sparkHost = sparkConfig.getString("host")
    lazy val sparkPort = sparkConfig.getString("port")
  }
}
