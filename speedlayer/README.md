# speedlayer

## run docker container
```
sudo docker run -d -p 4040:4040 -p 8000-9000:8000-9000 \
-e CASSANDRA_HOST=54.190.57.136 \
-e KAFKA_HOST=34.212.32.146   \
-e SPARK_MASTER_URL=spark://spark:7077 \
-e SPARK_WORKER_CORES=4 \-
e SPARK_EXECUTOR_MEMORY=8g \
-e SPARK_WORKER_INSTANCES=2  \
-h spark \
--name=speedlayer cquezadav/speedlayer
```
