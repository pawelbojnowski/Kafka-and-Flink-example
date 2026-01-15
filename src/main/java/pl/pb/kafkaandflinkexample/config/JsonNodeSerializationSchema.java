package pl.pb.kafkaandflinkexample.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.connector.kafka.sink.KafkaRecordSerializationSchema;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.jetbrains.annotations.Nullable;

public class JsonNodeSerializationSchema implements KafkaRecordSerializationSchema<JsonNode> {

    private String outputTopic;

    public JsonNodeSerializationSchema(String outputTopic) {
        this.outputTopic = outputTopic;
    }

    @Nullable
    @Override
    public ProducerRecord<byte[], byte[]> serialize(JsonNode element, KafkaSinkContext context, Long timestamp) {
        try {
            return new ProducerRecord<>(outputTopic, new ObjectMapper().writeValueAsBytes(element));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}