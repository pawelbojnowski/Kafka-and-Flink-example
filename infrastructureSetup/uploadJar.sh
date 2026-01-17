#!/bin/bash

cd ../
mvn clean package


pwd
curl -X POST -H "Expect:" \
  -F "jarfile=@$(pwd)/target/kafka-and-flink-example-1.0-SNAPSHOT.jar" \
  http://localhost:8084/jars/upload

#curl -X POST "http://localhost:8081/jars/kafka-and-flink-example/run" \
#  -H "Content-Type: application/json" \
#  -d '{
#    "entryClass": "pl.pb.kafkaandflinkexample.flink.FlinkExampleForRunOnFlinkServer",
#    "programArgs": "--arg1 foo --arg2 bar",
#    "parallelism": 1
#  }'