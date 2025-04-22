package pl.pb.kafkaandflinkexample.config;

import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.streams.StreamsConfig;

import java.util.Properties;
import java.util.UUID;

public class KafkaProperties {

    public static final String KAFKA_URL = "localhost:9091";
    public static final String APPLICATION_ID = "streams-example";
    public static final String GROUP_ID = "example-consumer-group-id";
    public static final String AUTO_OFFSET_RESET = "earliest";
    public static final String ENABLE_AUTO_COMMIT = "false";
    public static final String SCHEMA_REGISTRY_URL_PARAM = "schema.registry.url";
    public static final String SCHEMA_REGISTRY_URL = "http://localhost:8081";

    private KafkaProperties() {
    }

    public static Properties getConsumerConfig() {
        final Properties properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA_URL);
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID + UUID.randomUUID().toString());
        properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, AUTO_OFFSET_RESET);
        properties.setProperty(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, ENABLE_AUTO_COMMIT);
        properties.setProperty(SCHEMA_REGISTRY_URL_PARAM, SCHEMA_REGISTRY_URL);
        return properties;
    }

    public static Properties getStreamsConfig() {
        final Properties properties = new Properties();
        properties.putIfAbsent(StreamsConfig.APPLICATION_ID_CONFIG, APPLICATION_ID);
        properties.putIfAbsent(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA_URL);
        properties.putIfAbsent(StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG, 0);
        properties.setProperty(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, SpecificAvroSerde.class.getName());
        properties.setProperty(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, SpecificAvroSerde.class.getName());
        properties.putIfAbsent(StreamsConfig.NUM_STANDBY_REPLICAS_CONFIG, 1);
        properties.setProperty(SCHEMA_REGISTRY_URL_PARAM, SCHEMA_REGISTRY_URL);
        return properties;
    }

    public static Properties getProducerConfig() {
        final Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA_URL);
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class.getName());
        properties.setProperty(SCHEMA_REGISTRY_URL_PARAM, SCHEMA_REGISTRY_URL);
        return properties;
    }
}