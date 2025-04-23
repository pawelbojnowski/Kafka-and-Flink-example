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

create_topic() {
  local TOPIC_NAME=$1
  curl -X POST http://localhost:8082/v3/clusters/$(curl -s http://localhost:8082/v3/clusters | jq -r '.data[0].cluster_id')/topics \
    -H "Content-Type: application/json" \
    -d "{
      \"topic_name\": \"$TOPIC_NAME\",
      \"partitions_count\": 1,
      \"replication_factor\": 1,
      \"configs\": []
    }" && echo -e "\n✔️ Topic '$TOPIC_NAME' created\n"
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
docker-compose  -f docker-compose.yml up --build -d --remove-orphans


while [[ $(curl -s -H "Content-Type: application/json" -XGET 'http://localhost:8083/connectors') != "[]" ]];
do
  printf "."
  sleep 1
done
sleep 3
echo "\n----------------------------------------------------"

#Topic kafka_and_flink_example_input
create_topic "kafka_and_flink_example_input"

#Topic kafka_and_flink_example_output
create_topic "kafka_and_flink_example_output"

# 🗝️ Key input
register_schema "kafka_and_flink_example_input-key" "../src/main/schema/avro/Id.avsc"

# 🧾 Value input
register_schema "kafka_and_flink_example_input-value" "../src/main/schema/avro/User.avsc"

# 🗝️ Key output
register_schema "kafka_and_flink_example_output-key" "../src/main/schema/avro/Id.avsc"

# 🧾 Value output
register_schema "kafka_and_flink_example_output-value" "../src/main/schema/avro/Client.avsc"


sleep 2
echo "\n----------------------------------------------------"
echo "List topic:\n"
curl -s -H "Content-Type: application/vnd.kafka.v2+json" -XGET 'http://localhost:8082/topics' | json_pp








