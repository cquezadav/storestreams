

## Kafka operations
```
kafka-topics.sh --create --zookeeper localhost:2181 —replication-factor 1  --partitions 1 --topic storestreams

kafka-topics.sh --list --zookeeper localhost:2181

kafka-console-producer.sh --broker-list localhost:9092 --topic storestreams

kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic storestreams --from-beginning
```
