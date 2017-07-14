name := "storestreams"

version := "1.0"

scalaVersion := "2.11.11"

val sparkVersion = "2.1.0"



libraryDependencies ++= Seq(
  "org.apache.spark"        %%  "spark-core"                        % sparkVersion % "provided",
  "org.apache.spark"        %%  "spark-streaming"                   % sparkVersion, //% "provided",
  "org.apache.spark"        %%  "spark-sql"                         % sparkVersion, // % "provided",
  "org.apache.spark" 				%% 	"spark-streaming-kafka-0-10"		    % sparkVersion, // % "provided",
  "com.datastax.spark"      %%   "spark-cassandra-connector"        % "2.0.0-M3",
  "com.google.code.gson"    %    "gson"                             % "2.3.1",
  "com.typesafe.play"       %%   "play-json"                        % "2.4.6",
  "org.apache.kafka"        %    "kafka-clients"                    % "0.10.1.0"
)