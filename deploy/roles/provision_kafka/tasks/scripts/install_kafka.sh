#!/bin/bash

# Install kafka
sudo mkdir /opt/kafka
cd /opt/kafka/
sudo wget "http://www-us.apache.org/dist/kafka/0.11.0.0/kafka_2.12-0.11.0.0.tgz"
sudo tar -xvzf kafka_2.12-0.11.0.0.tgz  --strip-components 1
sudo rm -rf kafka_2.12-0.11.0.0.tgz kafka_2.11-0.11.0.0.tgz.asc
sudo mkdir /var/lib/kafka
sudo mkdir /var/lib/kafka/data
