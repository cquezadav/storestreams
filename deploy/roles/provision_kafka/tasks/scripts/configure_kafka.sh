#!/bin/bash

# Configure kafka
sudo sed -i 's/#delete.topic.enable=true/delete.topic.enable=true/' /opt/kafka/config/server.properties
sudo sed -i 's|log.dirs=/tmp/kafka-logs|log.dirs=/var/lib/kafka/data|' /opt/kafka/config/server.properties
sudo sed -i "$ a advertised.host.name=$1" /opt/kafka/config/server.properties
