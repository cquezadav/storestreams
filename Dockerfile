#FROM cquezadav/spark:2.1.1
FROM cquezadav/scala:2.11.11

COPY target/scala-2.11/storestreams-assembly-1.0.jar /app/storestreams-1.0.jar

ENV CASSANDRA_HOST="192.168.99.100"
ENV KAFKA_HOST="192.168.99.100"
ENV SPARK_MASTER="192.168.99.100"
ENV MESSAGES_TIME_WINDOW="2000"

WORKDIR /app

#CMD spark-submit --driver-memory 1g  --class storestreams.streaming.SpeedLayerState target/scala-2.11/storestreams-1.0.jar
CMD scala -classpath "storestreams-1.0.jar" storestreams.producer.MessageProducer