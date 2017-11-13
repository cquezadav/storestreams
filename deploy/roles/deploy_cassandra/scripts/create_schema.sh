#!/bin/bash

# Wait for cassandra to load before starting ddl
echo "Wait for cassandra to start with retry for 40 seconds"
attempt=0
while [ $attempt -le 20 ]; do
  attempt=$(( $attempt + 1 ))
  echo "Waiting for cassandra to startup (attempt: $attempt)..."
  result=$(sudo docker logs cassandra)
  if grep -q 'Starting listening for CQL clients' <<< $result ; then
    echo "Cassandra is up!"
    # create C* schema
    sudo docker cp /etc/schema.cql cassandra:schema.cql && docker exec cassandra cqlsh -f schema.cql
    break
  fi
  sleep 2
done
