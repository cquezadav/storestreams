#!/bin/bash
### This bash file starts zeppelin container if it is not running

ZEPPELIN_CONTAINER=zeppelin
if [ ! "$(docker ps -q -f name=${ZEPPELIN_CONTAINER})" ]; then
    if [ "$(docker ps -aq -f status=exited -f name=${ZEPPELIN_CONTAINER})" ]; then
        docker rm -f ${ZEPPELIN_CONTAINER}
    fi
    # run your container
    echo "starting ${ZEPPELIN_CONTAINER} container..."
    docker run -d --name ${ZEPPELIN_CONTAINER} -v $PWD/notebooks:/usr/zeppelin/notebook cquezadav/zeppelin
else
	echo "${ZEPPELIN_CONTAINER} container is already running"
fi

# start kafka
docker-compose -f docker-images/kafka-docker/docker-compose.yml up -d
