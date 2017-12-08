#!/bin/bash

start-master.sh
start-slave.sh spark://spark:7077

spark-submit --driver-memory 1g  --class streaming.SpeedLayerState speedlayer-1.0.jar
