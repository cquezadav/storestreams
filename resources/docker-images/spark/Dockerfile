FROM cquezadav/java:8.131.11

RUN apk update && apk upgrade && apk add bash && apk add curl
ARG SPARK_ARCHIVE=https://d3kbcqa49mib13.cloudfront.net/spark-2.1.1-bin-hadoop2.7.tgz
RUN curl -s $SPARK_ARCHIVE | tar -xz -C /usr/local/

ENV SPARK_HOME /usr/local/spark-2.1.1-bin-hadoop2.7
ENV PATH $PATH:$SPARK_HOME/bin

EXPOSE 4040 6066 7077 8080

WORKDIR $SPARK_HOME
