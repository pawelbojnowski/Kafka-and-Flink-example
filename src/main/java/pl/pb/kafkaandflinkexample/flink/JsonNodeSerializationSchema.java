package pl.pb.kafkaandflinkexample.flink;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.connector.kafka.sink.KafkaRecordSerializationSchema;
import org.apache.kafka.clients.producer.ProducerRecord;

public class JsonNodeSerializationSchema implements KafkaRecordSerializationSchema<JsonNode> {

    private ObjectMapper objectMapper;
    private String outputTopic;

    public JsonNodeSerializationSchema(String outputTopic) {
        this.outputTopic = outputTopic;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public ProducerRecord<byte[], byte[]> serialize(JsonNode element, KafkaRecordSerializationSchema.KafkaSinkContext context, Long timestamp) {
        try {
            return new ProducerRecord<>(outputTopic, objectMapper.writeValueAsBytes(element));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}