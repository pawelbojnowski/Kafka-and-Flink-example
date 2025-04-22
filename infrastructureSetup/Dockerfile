FROM confluentinc/cp-kafka-connect:7.3.3
RUN  confluent-hub install --no-prompt confluentinc/kafka-connect-protobuf-converter:7.3.3
RUN  confluent-hub install --no-prompt confluentinc/kafka-connect-avro-converter:7.3.3
RUN  confluent-hub install --no-prompt confluentinc/kafka-connect-jdbc:10.7.1