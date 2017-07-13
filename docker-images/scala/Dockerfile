#   Alpine-based Scala container for development includes:
#     - JDK
#     - SBT
#     - Bash (NOTE: bash is used by scala/scalac scripts, and it cannot be easily replaced with ash.)

FROM cquezadav/java:8.131.11

MAINTAINER "Carlos Quezada"

ENV SCALA_VERSION="2.11.11"
ENV SBT_VERSION="0.13.15"
ENV SCALA_HOME="/usr/share/scala"
ENV SBT_HOME="/usr/share/sbt"

RUN apk add --no-cache --virtual=.build-dependencies wget ca-certificates && \
    apk add --no-cache bash && \
    cd "/tmp" && \
    wget "https://downloads.typesafe.com/scala/${SCALA_VERSION}/scala-${SCALA_VERSION}.tgz" && \
    tar xzf "scala-${SCALA_VERSION}.tgz" && \
    mkdir "${SCALA_HOME}" && \
    rm "/tmp/scala-${SCALA_VERSION}/bin/"*.bat && \
    mv "/tmp/scala-${SCALA_VERSION}/bin" "/tmp/scala-${SCALA_VERSION}/lib" "${SCALA_HOME}" && \
    ln -s "${SCALA_HOME}/bin/"* "/usr/bin/" && \
    wget "http://dl.bintray.com/sbt/native-packages/sbt/${SBT_VERSION}/sbt-${SBT_VERSION}.tgz" && \
    tar xzf "sbt-${SBT_VERSION}.tgz" && \
    mv "/tmp/sbt" "${SBT_HOME}" && \
    ln -s "${SBT_HOME}/bin/"* "/usr/bin/" && \
    apk del .build-dependencies && \
rm -rf "/tmp/"*