FROM cquezadav/spark:2.1.1

COPY target/scala-2.11/storestreams-assembly-1.0.jar /app/target/scala-2.11/storestreams-1.0.jar

ENV _CASSANDRA_HOST="${_CASSANDRA_HOST}"
ENV _KAFKA_HOST="${_KAFKA_HOST}"
ENV _SPARK_MASTER="${_SPARK_MASTER}"

WORKDIR /app

CMD spark-submit --driver-memory 1g  --class storestreams.streaming.SpeedLayerState target/scala-2.11/storestreams-1.0.jar