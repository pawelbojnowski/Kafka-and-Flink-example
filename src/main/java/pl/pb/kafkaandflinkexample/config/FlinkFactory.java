package pl.pb.kafkaandflinkexample.config;

import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.connector.kafka.sink.KafkaSink;
import org.apache.flink.connector.kafka.source.KafkaSource;
import pl.pb.kafkaandflinkexample.flink.AvroKeyValueDeserializationSchema;
import pl.pb.kafkaandflinkexample.flink.AvroKeyValueSerializationSchema;

import java.util.UUID;

import static pl.pb.kafkaandflinkexample.config.FlinkProperties.GROUP_ID;
import static pl.pb.kafkaandflinkexample.config.FlinkProperties.getPropertiesSource;

public class FlinkFactory {
    private FlinkFactory() {
    }

    public static <K, V> KafkaSource buildKafkaSource(String topic, Class<K> key, Class<V> value, boolean flinkEnv) {
        return KafkaSource.<Tuple2<K, V>>builder()
                .setBootstrapServers(FlinkProperties.KAFKA_URL)
                .setTopics(topic)
                .setGroupId(GROUP_ID + UUID.randomUUID())
                .setDeserializer(new AvroKeyValueDeserializationSchema(key, value))
                .setProperties(getPropertiesSource(flinkEnv))
                .build();
    }

    public static <K, V> KafkaSink buildKafkaSink(String outputTopic, Class<K> keyClass, Class<V> valueClass, boolean flinkEnv) {
        return KafkaSink.<Tuple2<K, V>>builder()
                .setBootstrapServers(FlinkProperties.KAFKA_URL)
                .setRecordSerializer(new AvroKeyValueSerializationSchema(outputTopic, keyClass, valueClass))
                .setKafkaProducerConfig(FlinkProperties.getPropertiesSink(flinkEnv))
                .build();
    }
}