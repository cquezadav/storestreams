#FROM cquezadav/spark:2.1.1
FROM cquezadav/scala:2.11.11

#COPY messagesproducer/target/scala-2.11/messagesproducer-assembly-1.0.jar /app/messagesproducer-1.0.jar

ENV CASSANDRA_HOST="192.168.99.100"
ENV KAFKA_HOST="192.168.99.100"
ENV SPARK_MASTER="192.168.99.100"
ENV MESSAGES_TIME_WINDOW="2000"

WORKDIR /app

#CMD spark-submit --driver-memory 1g  --class storestreams.streaming.SpeedLayerState target/scala-2.11/storestreams-1.0.jar
#CMD scala -classpath "messagesproducer-1.0.jar" producer.MessageProducer