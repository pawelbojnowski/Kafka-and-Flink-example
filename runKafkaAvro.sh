#!/bin/bash


#remove existing docker images
ids=$(docker ps -aqf name=kafka_and_flink_example_.)
for id in $(echo $ids | tr "\n" " "); do
  docker stop  $id
  docker container rm -f $id
done

#remove existing docker volumes
rm -rf ../volumes

#create docker
docker-compose -f docker-compose-avro.yml up --build -d --remove-orphans


while [[ $(curl -s -H "Content-Type: application/json" -XGET 'http://localhost:8083/connectors') != "[]" ]];
do
  printf "."
  sleep 1
done
sleep 3

echo "\ntopics: "
curl -X POST http://localhost:8082/v3/clusters/$(curl -s http://localhost:8082/v3/clusters | jq -r '.data[0].cluster_id')/topics \
  -H "Content-Type: application/json" \
  -d '{
    "topic_name": "kafka_and_flink_example_input",
    "partitions_count": 1,
    "replication_factor": 1,
    "configs": []
  }'
echo "\n"
curl -X POST http://localhost:8082/v3/clusters/$(curl -s http://localhost:8082/v3/clusters | jq -r '.data[0].cluster_id')/topics \
  -H "Content-Type: application/json" \
  -d '{
    "topic_name": "kafka_and_flink_example_output",
    "partitions_count": 1,
    "replication_factor": 1,
    "configs": []
  }'



echo "\nkafka_and_flink_example_input-key: "
curl -X POST http://localhost:8081/subjects/kafka_and_flink_example_input-key/versions \
  -H "Content-Type: application/vnd.schemaregistry.v1+json" \
  -d '{ "schema": "{\"type\" : \"record\", \"name\" : \"Id\", \"namespace\" : \"pl.pb.kafkamodel.avro\", \"fields\" : [ {\"name\" : \"id\", \"type\" : \"int\"} ]}"}'

echo "\nkafka_and_flink_example_input-value: "
curl -X POST http://localhost:8081/subjects/kafka_and_flink_example_input-value/versions \
  -H "Content-Type: application/vnd.schemaregistry.v1+json" \
  -d '{ "schema": "{\"type\" : \"record\", \"name\" : \"User\", \"namespace\" : \"pl.pb.kafkamodel.avro\", \"fields\" : [ {\"name\" : \"id\", \"type\" : {\"type\" : \"record\", \"name\" : \"Id\", \"fields\" : [ {\"name\" : \"id\", \"type\" : \"int\"} ]}}, {\"name\" : \"firstname\", \"type\" : \"string\"}, {\"name\" : \"lastname\", \"type\" : \"string\"}, {\"name\" : \"phone_number\", \"type\" : \"int\"} ]}"}'

#echo "\nkafka_and_flink_example_output-key: "
#curl -X POST http://localhost:8081/subjects/kafka_and_flink_example_output-key/versions \
#  -H "Content-Type: application/vnd.schemaregistry.v1+json" \
#  -d '{ "schema": "{\"type\" : \"record\", \"name\" : \"Id\", \"namespace\" : \"pl.pb.kafkamodel.avro\", \"fields\" : [ {\"name\" : \"id\", \"type\" : \"int\"} ]}"}'
#
#echo "\nkafka_and_flink_example_output-value: "
#curl -X POST http://localhost:8081/subjects/kafka_and_flink_example_output-value/versions \
#  -H "Content-Type: application/vnd.schemaregistry.v1+json" \
#  -d '{ "schema": "{\"type\" : \"record\", \"name\" : \"Client\", \"namespace\" : \"pl.pb.kafkamodel.avro\", \"fields\" : [ {\"name\" : \"userId\", \"type\" : \"string\"}, {\"name\" : \"id\", \"type\" : {\"type\" : \"record\", \"name\" : \"Id\", \"fields\" : [ {\"name\" : \"id\", \"type\" : \"int\"} ]}}, {\"name\" : \"firstname\", \"type\" : \"string\"}, {\"name\" : \"lastname\", \"type\" : \"string\"}, {\"name\" : \"phone_number\", \"type\" : \"int\"} ]}"}'


echo "\n----------------------------------------------------"
echo "List topic:\n"
curl -s -H "Content-Type: application/vnd.kafka.v2+json" -XGET 'http://localhost:8082/topics' | json_pp








