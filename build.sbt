name := "storestreams"

version := "1.0"

scalaVersion := "2.11.11"

val sparkVersion = "2.1.1"

libraryDependencies ++= Seq(
  "org.apache.spark"        %%  "spark-core"                        % sparkVersion % "provided",
  "org.apache.spark"        %%  "spark-streaming"                   % sparkVersion % "provided",
  "org.apache.spark"        %%  "spark-sql"                         % sparkVersion % "provided",
  //"org.apache.spark"        %%  "spark-streaming"                   % sparkVersion,
  //"org.apache.spark"        %%  "spark-sql"                         % sparkVersion,
  "org.apache.spark" 				%% 	"spark-streaming-kafka-0-10"		    % sparkVersion,
  "com.datastax.spark"      %%   "spark-cassandra-connector"        % "2.0.0",
  "com.google.code.gson"    %    "gson"                             % "2.3.1",
  "com.typesafe.play"       %%   "play-json"                        % "2.4.6",
  "org.apache.kafka"        %    "kafka-clients"                    % "0.10.1.0"
)

// deal with duplicates
assemblyMergeStrategy in assembly := {
  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
  case x => MergeStrategy.first
}

//run in Compile := Defaults.runTask(fullClasspath in Compile, mainClass in (Compile, run), runner in (Compile, run)).evaluated
//runMain in Compile := Defaults.runMainTask(fullClasspath in Compile, runner in(Compile, run)).evaluated