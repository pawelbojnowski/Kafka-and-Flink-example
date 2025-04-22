package pl.pb.kafkaandflinkexample.config;

import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import org.apache.avro.specific.SpecificRecord;
import pl.pb.kafkamodel.avro.Client;
import pl.pb.kafkamodel.avro.Id;
import pl.pb.kafkamodel.avro.User;

import java.util.Map;

import static pl.pb.kafkaandflinkexample.config.KafkaProperties.SCHEMA_REGISTRY_URL;
import static pl.pb.kafkaandflinkexample.config.KafkaProperties.SCHEMA_REGISTRY_URL_PARAM;

public class KafkaSerde {
    private static final Map<String, String> serdeConfig = Map.of(SCHEMA_REGISTRY_URL_PARAM, SCHEMA_REGISTRY_URL);

    public static final SpecificAvroSerde<Id> KEY_SERDE_ID = createSpecificAvroSerde(true);
    public static final SpecificAvroSerde<User> VALUE_SERDE_USER = createSpecificAvroSerde(false);
    public static final SpecificAvroSerde<Client> VALUE_SERDE_CLIENT = createSpecificAvroSerde(false);


    private static <T extends SpecificRecord> SpecificAvroSerde<T> createSpecificAvroSerde(boolean isSerdeForRecordKeys) {
        SpecificAvroSerde<T> valueSerdeClient = new SpecificAvroSerde<T>();
        valueSerdeClient.configure(serdeConfig, isSerdeForRecordKeys);
        return valueSerdeClient;
    }
}
