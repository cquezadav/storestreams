FROM cquezadav/spark:2.1.1

COPY target/scala-2.11/speedlayer-assembly-1.0.jar /app/speedlayer-1.0.jar

ENV CASSANDRA_HOST="192.168.99.100"
ENV KAFKA_HOST="192.168.99.100"
ENV SPARK_MASTER="local[*]"
ENV MESSAGES_TIME_WINDOW="2000"

WORKDIR /app

CMD spark-submit --driver-memory 2g  --class streaming.SpeedLayerState speedlayer-1.0.jar