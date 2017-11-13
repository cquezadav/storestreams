#!/bin/bash

# Launch Spark master and one slave worker
/usr/local/spark-2.1.1-bin-hadoop2.7/sbin/start-master.sh
/usr/local/spark-2.1.1-bin-hadoop2.7/sbin/start-slave.sh spark://spark-master:7077 --webui-port 8081

spark-submit --driver-memory 2g  --class streaming.SpeedLayerState speedlayer-1.0.jar