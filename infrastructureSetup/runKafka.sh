#!/bin/bash

register_schema() {
  local subject=$1
  local schema_file=$2

  echo -e "\n🔧 Rejestruję: $subject (plik: $schema_file)"

  curl -s -X POST "http://localhost:8081/subjects/$subject/versions" \
    -H "Content-Type: application/vnd.schemaregistry.v1+json" \
    -d "{\"schema\": $(jq -Rs . < "$schema_file")}" \
    | jq .
}

#remove existing docker images
ids=$(docker ps -aqf name=KafkaAndFlinkExample.)
for id in $(echo $ids | tr "\n" " "); do
  docker stop  $id
  docker container rm -f $id
done

#remove existing docker volumes
rm -rf ./volumes

#create docker
docker-compose  -f $(dirname "$0")/docker-compose.yml up --build -d --remove-orphans


while [[ $(curl -s -H "Content-Type: application/json" -XGET 'http://localhost:8083/connectors') != "[]" ]];
do
  printf "."
  sleep 1
done
sleep 3
echo "\n----------------------------------------------------"

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


# 🗝️ Klucz input
register_schema "kafka_and_flink_example_input-key" "../src/main/schema/avro/Id.avsc"

# 🧾 Wartość input
register_schema "kafka_and_flink_example_input-value" "../src/main/schema/avro/User.avsc"

# 🗝️ Klucz output
register_schema "kafka_and_flink_example_output-key" "../src/main/schema/avro/Id.avsc"

# 🧾 Wartość output
register_schema "kafka_and_flink_example_output-value" "../src/main/schema/avro/Client.avsc"


sleep 2
echo "\n----------------------------------------------------"
echo "List topic:\n"
curl -s -H "Content-Type: application/vnd.kafka.v2+json" -XGET 'http://localhost:8082/topics' | json_pp








