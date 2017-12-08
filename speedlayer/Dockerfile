FROM cquezadav/openjdk_spark:2.1.1

ENV CASSANDRA_HOST="192.168.99.100"
ENV KAFKA_HOST="192.168.99.100"
ENV SPARK_MASTER_URL="spark://spark:7077"
ENV MESSAGES_TIME_WINDOW="2000"

COPY target/scala-2.11/speedlayer-assembly-1.0.jar /root/speedlayer-1.0.jar

COPY launch.sh /root/
RUN chmod +x /root/launch.sh

CMD ["./launch.sh"]
