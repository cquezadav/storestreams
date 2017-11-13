#!/bin/bash

# Create kafka service
sudo touch /etc/systemd/system/kafka.service
printf '[Unit]\nDescription=High-available, distributed message broker\nAfter=network.target\n[Service]\nType=simple\nExecStart=/opt/kafka/bin/kafka-server-start.sh /opt/kafka/config/server.properties\nExecStop=/opt/kafka/bin/kafka-server-stop.sh\nRestart=on-failure\nSyslogIdentifier=kafka\n[Install]\nWantedBy=multi-user.target' | sudo tee /etc/systemd/system/kafka.service
