package pl.pb.kafkaandflinkexample.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;

import java.util.Properties;
import java.util.UUID;

public class FlinkProperties {

    public static final String GROUP_ID = "flink_group_" + UUID.randomUUID();
    public static final String KAFKA_URL_LOCAL = "127.0.0.1:9092";
    public static final String KAFKA_URL = "KafkaAndFlinkExampleKafka:9092";
    public static final String SCHEMA_REGISTRY_URL_LOCAL = "http://127.0.0.1:8081";
    public static final String SCHEMA_REGISTRY_URL = "http://KafkaAndFlinkExampleSchemaRegistry:8081";
    public static final String SCHEMA_REGISTRY_URL_PARAM = "schema.registry.url";
    public static final String SPECIFIC_AVRO_READER = "specific.avro.reader";
    public static final String USE_GENERATED_CLASSES = "true";

    private FlinkProperties() {
    }

    public static Properties getPropertiesSource(boolean flinkEnv) {
        Properties properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, flinkEnv ? KAFKA_URL : KAFKA_URL_LOCAL);
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID + UUID.randomUUID().toString());
        properties.setProperty("taskmanager.cpu.cores", "MAX"); // maksymalna dostępna liczba rdzeni
        properties.setProperty("taskmanager.memory.task.heap.size", "MAX"); // cała dostępna pamięć heap
        properties.setProperty("taskmanager.memory.task.off-heap.size", "MAX"); // brak limitu off-heap
        properties.setProperty("taskmanager.memory.network.min", "64mb"); // domyślna wartość
        properties.setProperty("taskmanager.memory.network.max", "64mb"); // domyślna wartość
        properties.setProperty("taskmanager.memory.managed.size", "128mb"); // domyślna wartość
        properties.setProperty(SCHEMA_REGISTRY_URL_PARAM, flinkEnv ? SCHEMA_REGISTRY_URL : SCHEMA_REGISTRY_URL_LOCAL);
        properties.setProperty(SPECIFIC_AVRO_READER, USE_GENERATED_CLASSES);
        return properties;
    }

    public static Properties getPropertiesSink(boolean flinkEnv) {
        Properties properties = new Properties();
        properties.setProperty(SCHEMA_REGISTRY_URL_PARAM, flinkEnv ? SCHEMA_REGISTRY_URL : SCHEMA_REGISTRY_URL_LOCAL);
        properties.setProperty(SPECIFIC_AVRO_READER, USE_GENERATED_CLASSES);
        return properties;
    }
}