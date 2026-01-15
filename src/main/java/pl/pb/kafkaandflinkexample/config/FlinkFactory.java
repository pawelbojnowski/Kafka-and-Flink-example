package pl.pb.kafkaandflinkexample.config;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.flink.connector.kafka.sink.KafkaSink;
import org.apache.flink.connector.kafka.source.KafkaSource;
import pl.pb.kafkaandflinkexample.flink.JsonNodeDeserializationSchema;
import pl.pb.kafkaandflinkexample.flink.JsonNodeSerializationSchema;

import java.util.UUID;

public class FlinkFactory {
    private FlinkFactory() {
    }

    public static KafkaSource buildKafkaSource(String topic, boolean flinkEnv) {
        return KafkaSource.<JsonNode>builder()
                .setBootstrapServers(FlinkProperties.getKafkaUrl(flinkEnv))
                .setTopics(topic)
                .setGroupId(FlinkProperties.GROUP_ID + UUID.randomUUID())
                .setValueOnlyDeserializer(new JsonNodeDeserializationSchema())
                .setProperties(FlinkProperties.getPropertiesSource(flinkEnv))
                .build();
    }


    public static KafkaSink buildKafkaSink(String outputTopic, boolean flinkEnv) {
        return KafkaSink.<JsonNode>builder()
                .setBootstrapServers(FlinkProperties.getKafkaUrl(flinkEnv))
                .setRecordSerializer(new JsonNodeSerializationSchema(outputTopic))
                .setKafkaProducerConfig(FlinkProperties.getPropertiesSink(flinkEnv))
                .build();
    }
}