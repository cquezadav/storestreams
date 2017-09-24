val sparkVersion = "2.1.1"
//val scalaVersion = "2.11.11"

//TODO: just import libs needed by each module
lazy val baseSettings = Seq(
  version := "1.0",
  scalaVersion := "2.11.11"
)

lazy val baseDependencies = Seq(
  libraryDependencies ++= Seq(
    "com.typesafe.play" %% "play-json" % "2.4.6"
  ),
  assemblyMergeStrategy in assembly := {
    case PathList("META-INF", xs@_*) => MergeStrategy.discard
    case x => MergeStrategy.first
  }
)

lazy val root = project.in(file("."))
  .settings(
    name := "storestreams",
    moduleName := "storestreams"
  )
  .aggregate(utils, model, rawDataConsumer, messagesProducer, speedLayer, batchLayer)

lazy val model = project.in(file("model"))
  .settings(baseSettings)
  .settings(baseDependencies)
  .settings(
    name := "model",
    moduleName := "model"
  )

lazy val utils = project.in(file("utils"))
  .settings(baseSettings)
  .settings(baseDependencies)
  .settings(
    name := "utils",
    moduleName := "utils",
    libraryDependencies ++= Seq(
      "org.apache.spark" %% "spark-core" % sparkVersion,
      "org.apache.spark" %% "spark-streaming" % sparkVersion,
      "org.apache.spark" %% "spark-sql" % sparkVersion,
      "com.datastax.spark" %% "spark-cassandra-connector" % "2.0.0"
    )
  )

lazy val rawDataConsumer = project.in(file("rawdataconsumer"))
  .settings(baseSettings)
  .settings(baseDependencies)
  .settings(
    name := "rawdataconsumer",
    moduleName := "rawdataconsumer",
    libraryDependencies ++= Seq(
      "com.datastax.spark" %% "spark-cassandra-connector" % "2.0.0",
      "com.google.code.gson" % "gson" % "2.3.1",
      "org.apache.kafka" % "kafka-clients" % "0.10.1.0",
      "com.codahale.metrics" % "metrics-json" % "3.0.1"
    )
  )
  .dependsOn(utils, model)

lazy val messagesProducer = project.in(file("messagesproducer"))
  .settings(baseSettings)
  .settings(baseDependencies)
  .settings(
    name := "messagesproducer",
    moduleName := "messagesproducer",
    libraryDependencies ++= Seq(
      "com.google.code.gson" % "gson" % "2.3.1",
      "org.apache.kafka" % "kafka-clients" % "0.10.1.0"
    )
  )
  .dependsOn(utils, model)

lazy val speedLayer = project.in(file("speedlayer"))
  .settings(baseSettings)
  .settings(baseDependencies)
  .settings(
    name := "speedlayer",
    moduleName := "speedlayer",
    libraryDependencies ++= Seq(
      "org.apache.spark" %% "spark-core" % sparkVersion % "provided",
      //"org.apache.spark" %% "spark-streaming" % sparkVersion % "provided",
      //"org.apache.spark" %% "spark-sql" % sparkVersion % "provided",
      "org.apache.spark" %% "spark-streaming" % sparkVersion,
      "org.apache.spark" %% "spark-sql" % sparkVersion,
      "org.apache.spark" %% "spark-streaming-kafka-0-10" % sparkVersion,
      "com.datastax.spark" %% "spark-cassandra-connector" % "2.0.0"
    )
  )
  .dependsOn(utils, model)

lazy val batchLayer = project.in(file("batchlayer"))
  .settings(baseSettings)
  .settings(baseDependencies)
  .settings(
    name := "batchlayer",
    moduleName := "batchlayer"
  )
  .dependsOn(utils, model)

lazy val servingLayer = project.in(file("servinglayer"))
  .settings(baseSettings)
  .settings(baseDependencies)
  .settings(
    name := "servinglayer",
    moduleName := "servinglayer"
  )
  .dependsOn(utils, model)