package pl.pb.kafkaandflinkexample.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;

import java.util.Properties;
import java.util.UUID;

public class FlinkProperties {

    public static final String GROUP_ID = "flink_group_" + UUID.randomUUID();
    private static final String KAFKA_URL_LOCAL = "127.0.0.1:9091";
    private static final String KAFKA_URL = "http://KafkaAndFlinkExampleKafka:9091";

    private FlinkProperties() {
    }

    public static Properties getPropertiesSource(boolean flinkEnv) {
        Properties properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, getKafkaUrl(flinkEnv));
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID + UUID.randomUUID().toString());
        properties.setProperty("taskmanager.cpu.cores", "MAX");
        properties.setProperty("taskmanager.memory.task.heap.size", "MAX");
        properties.setProperty("taskmanager.memory.task.off-heap.size", "MAX");
        properties.setProperty("taskmanager.memory.network.min", "64mb");
        properties.setProperty("taskmanager.memory.network.max", "64mb");
        properties.setProperty("taskmanager.memory.managed.size", "128mb");
        return properties;
    }

    public static String getKafkaUrl(boolean flinkEnv) {
        return flinkEnv ? KAFKA_URL : KAFKA_URL_LOCAL;
    }

    public static Properties getPropertiesSink(boolean flinkEnv) {
        Properties properties = new Properties();
        return properties;
    }
}