package pl.pb.kafkaandflinkexample.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;

import java.util.Properties;
import java.util.UUID;

public class FlinkProperties {

    public static final String KAFKA_URL = "localhost:9091";
    public static final String GROUP_ID = "flink-group";
    public static final String SCHEMA_REGISTRY_URL_PARAM = "schema.registry.url";
    public static final String SCHEMA_REGISTRY_URL = "http://localhost:8081";
    public static final String SPECIFIC_AVRO_READER = "specific.avro.reader";
    public static final String USE_GENERATED_CLASSES = "true";

    private FlinkProperties() {
    }

    public static Properties getPropertiesSource() {
        Properties properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA_URL);
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID + UUID.randomUUID().toString());
        properties.setProperty("taskmanager.cpu.cores", "MAX"); // maksymalna dostępna liczba rdzeni
        properties.setProperty("taskmanager.memory.task.heap.size", "MAX"); // cała dostępna pamięć heap
        properties.setProperty("taskmanager.memory.task.off-heap.size", "MAX"); // brak limitu off-heap
        properties.setProperty("taskmanager.memory.network.min", "64mb"); // domyślna wartość
        properties.setProperty("taskmanager.memory.network.max", "64mb"); // domyślna wartość
        properties.setProperty("taskmanager.memory.managed.size", "128mb"); // domyślna wartość
        properties.setProperty(SCHEMA_REGISTRY_URL_PARAM, SCHEMA_REGISTRY_URL);
        properties.setProperty(SPECIFIC_AVRO_READER, USE_GENERATED_CLASSES);
        return properties;
    }

    public static Properties getPropertiesSink() {
        Properties properties = new Properties();
        properties.setProperty(SCHEMA_REGISTRY_URL_PARAM, SCHEMA_REGISTRY_URL);
        properties.setProperty(SPECIFIC_AVRO_READER, USE_GENERATED_CLASSES);
        return properties;
    }
}