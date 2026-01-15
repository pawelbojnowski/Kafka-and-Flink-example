package pl.pb.kafkaandflinkexample.flink;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.api.common.serialization.AbstractDeserializationSchema;

import java.io.IOException;

public class JsonNodeDeserializationSchema extends AbstractDeserializationSchema<JsonNode> {
    @Override
    public JsonNode deserialize(byte[] bytes) throws IOException {
        return new ObjectMapper().readTree(bytes);
    }
}