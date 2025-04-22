package pl.pb.kafkaandflinkexample.flink;

import org.apache.avro.specific.SpecificRecord;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.connector.kafka.sink.KafkaRecordSerializationSchema;
import org.apache.flink.formats.avro.registry.confluent.ConfluentRegistryAvroSerializationSchema;
import org.apache.kafka.clients.producer.ProducerRecord;
import pl.pb.kafkaandflinkexample.config.KafkaProperties;

public class AvroKeyValueSerializationSchema<K, V> implements KafkaRecordSerializationSchema<Tuple2<K, V>> {

    private final String topic;
    private final ConfluentRegistryAvroSerializationSchema keySchema;
    private final ConfluentRegistryAvroSerializationSchema valueSchema;

    public AvroKeyValueSerializationSchema(String topic, Class<SpecificRecord> keyClass, Class<SpecificRecord> valueClass) {
        this.topic = topic;
        this.keySchema = ConfluentRegistryAvroSerializationSchema.forSpecific(keyClass, topic + "-key", KafkaProperties.SCHEMA_REGISTRY_URL);
        this.valueSchema = ConfluentRegistryAvroSerializationSchema.forSpecific(valueClass, topic + "-value", KafkaProperties.SCHEMA_REGISTRY_URL);
    }

    @Override
    public ProducerRecord<byte[], byte[]> serialize(Tuple2<K, V> element, KafkaSinkContext context, Long timestamp) {
        byte[] keyBytes = keySchema.serialize(element.f0);
        byte[] valueBytes = valueSchema.serialize(element.f1);
        return new ProducerRecord<>(topic, keyBytes, valueBytes);
    }
}
