package pl.pb.kafkaandflinkexample.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.api.common.serialization.AbstractDeserializationSchema;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.common.typeinfo.Types;

import java.io.IOException;

public class JsonNodeDeserializationSchema extends AbstractDeserializationSchema<JsonNode> {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Override
    public JsonNode deserialize(byte[] message) throws IOException {
        return MAPPER.readTree(message);
    }

    @Override
    public TypeInformation<JsonNode> getProducedType() {
        // JsonNode nie jest POJO -> GenericType
        return Types.GENERIC(JsonNode.class);
    }
}