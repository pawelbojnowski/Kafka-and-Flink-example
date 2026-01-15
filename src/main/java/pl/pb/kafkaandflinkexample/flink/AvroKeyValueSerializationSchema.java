package pl.pb.kafkaandflinkexample.flink;

import org.apache.avro.specific.SpecificRecord;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.connector.kafka.sink.KafkaRecordSerializationSchema;
import org.apache.flink.formats.avro.registry.confluent.ConfluentRegistryAvroSerializationSchema;
import org.apache.kafka.clients.producer.ProducerRecord;
import pl.pb.kafkaandflinkexample.config.FlinkProperties;

public class AvroKeyValueSerializationSchema<K, V> implements KafkaRecordSerializationSchema<Tuple2<K, V>> {

    private final String topic;
    private final ConfluentRegistryAvroSerializationSchema keySchema;
    private final ConfluentRegistryAvroSerializationSchema valueSchema;

    public AvroKeyValueSerializationSchema(String topic, Class<SpecificRecord> keyClass, Class<SpecificRecord> valueClass, Boolean flinkEnv) {
        this.topic = topic;
        this.keySchema = ConfluentRegistryAvroSerializationSchema.forSpecific(keyClass, topic + "-key", FlinkProperties.getSchemaRegistryUrl(flinkEnv));
        this.valueSchema = ConfluentRegistryAvroSerializationSchema.forSpecific(valueClass, topic + "-value", FlinkProperties.getSchemaRegistryUrl(flinkEnv));
    }

    @Override
    public ProducerRecord<byte[], byte[]> serialize(Tuple2<K, V> element, KafkaSinkContext context, Long timestamp) {
        final byte[] keyBytes = keySchema.serialize(element.f0);
        final byte[] valueBytes = valueSchema.serialize(element.f1);
        return new ProducerRecord<>(topic, keyBytes, valueBytes);
    }
}
