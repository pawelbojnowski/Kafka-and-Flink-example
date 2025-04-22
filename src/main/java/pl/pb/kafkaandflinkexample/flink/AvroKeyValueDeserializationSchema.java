package pl.pb.kafkaandflinkexample.flink;

import org.apache.avro.specific.SpecificRecord;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.connector.kafka.source.reader.deserializer.KafkaRecordDeserializationSchema;
import org.apache.flink.formats.avro.registry.confluent.ConfluentRegistryAvroDeserializationSchema;
import org.apache.flink.util.Collector;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.io.IOException;

import static pl.pb.kafkaandflinkexample.config.KafkaProperties.SCHEMA_REGISTRY_URL;

public class AvroKeyValueDeserializationSchema<K, V> implements KafkaRecordDeserializationSchema<Tuple2<K, V>> {

    private final ConfluentRegistryAvroDeserializationSchema keyDeserializer;
    private final ConfluentRegistryAvroDeserializationSchema valueDeserializer;
    private final TypeInformation<Tuple2<K, V>> producedType;

    public AvroKeyValueDeserializationSchema(Class<SpecificRecord> key, Class<SpecificRecord> value) {
        this.keyDeserializer = ConfluentRegistryAvroDeserializationSchema.forSpecific(key, SCHEMA_REGISTRY_URL);
        this.valueDeserializer = ConfluentRegistryAvroDeserializationSchema.forSpecific(value, SCHEMA_REGISTRY_URL);
        this.producedType = Types.TUPLE(TypeInformation.of(key), TypeInformation.of(value));
    }

    @Override
    public void deserialize(ConsumerRecord<byte[], byte[]> record, Collector<Tuple2<K, V>> out) throws IOException {
        K key = (K) keyDeserializer.deserialize(record.key());
        V value = (V) valueDeserializer.deserialize(record.value());
        out.collect(Tuple2.of(key, value));
    }

    @Override
    public TypeInformation<Tuple2<K, V>> getProducedType() {
        return producedType;
    }
}
