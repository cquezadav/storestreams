val sparkVersion = "2.1.1"
//TODO: just import libs needed by each module
lazy val baseSettings = Seq(
  version := "1.0",
  scalaVersion := "2.11.11",
  libraryDependencies ++= Seq(
    "org.apache.spark" %% "spark-core" % sparkVersion % "provided",
    //    "org.apache.spark" %% "spark-streaming" % sparkVersion % "provided",
    //    "org.apache.spark" %% "spark-sql" % sparkVersion % "provided",
    "org.apache.spark"        %%  "spark-streaming"                   % sparkVersion,
    "org.apache.spark"        %%  "spark-sql"                         % sparkVersion,
    "org.apache.spark" %% "spark-streaming-kafka-0-10" % sparkVersion,
    "com.datastax.spark" %% "spark-cassandra-connector" % "2.0.0",
    "com.google.code.gson" % "gson" % "2.3.1",
    "com.typesafe.play" %% "play-json" % "2.4.6",
    "org.apache.kafka" % "kafka-clients" % "0.10.1.0"//,
    //"io.netty" % "netty-transport-native-epoll" % "4.0.27.Final" classifier "linux-x86_64"
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
  .settings(
    name := "model",
    moduleName := "model",
    libraryDependencies ++= Seq(
      "com.typesafe.play" %% "play-json" % "2.4.6"
    )
  )

lazy val utils = project.in(file("utils"))
  .settings(baseSettings)
  .settings(
    name := "utils",
    moduleName := "utils"
  )

lazy val rawDataConsumer = project.in(file("rawdataconsumer"))
  .settings(baseSettings)
  .settings(
    name := "rawdataconsumer",
    moduleName := "rawdataconsumer",
    libraryDependencies ++= Seq(
      "com.codahale.metrics" % "metrics-json" % "3.0.1"
    )
  )
  .aggregate(utils, model)
  .dependsOn(utils, model)

lazy val messagesProducer = project.in(file("messagesproducer"))
  .settings(baseSettings)
  .settings(
    name := "messagesproducer",
    moduleName := "messagesproducer"
  )
  .aggregate(utils, model)
  .dependsOn(utils, model)

lazy val speedLayer = project.in(file("speedlayer"))
  .settings(baseSettings)
  .settings(
    name := "speedlayer",
    moduleName := "speedlayer"
  )
  .aggregate(utils, model)
  .dependsOn(utils, model)

lazy val batchLayer = project.in(file("batchlayer"))
  .settings(baseSettings)
  .settings(
    name := "batchlayer",
    moduleName := "batchlayer"
  )
  .aggregate(utils, model)
  .dependsOn(utils, model)


//run in Compile := Defaults.runTask(fullClasspath in Compile, mainClass in (Compile, run), runner in (Compile, run)).evaluated
//runMain in Compile := Defaults.runMainTask(fullClasspath in Compile, runner in(Compile, run)).evaluated