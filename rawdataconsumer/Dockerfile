FROM cquezadav/scala:2.11.11

COPY target/scala-2.11/rawdataconsumer-assembly-1.0.jar /app/rawdataconsumer-1.0.jar

ENV CASSANDRA_HOST="192.168.99.100"
ENV KAFKA_HOST="192.168.99.100"
ENV SPARK_MASTER="192.168.99.100"
ENV MESSAGES_TIME_WINDOW="2000"

WORKDIR /app

CMD scala -classpath "rawdataconsumer-1.0.jar" consumer.RawDataConsumer
