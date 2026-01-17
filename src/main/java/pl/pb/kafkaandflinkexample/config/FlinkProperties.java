package pl.pb.kafkaandflinkexample.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;

import java.util.Properties;
import java.util.UUID;

public class FlinkProperties {

    public static final String GROUP_ID = "flink_group_" + UUID.randomUUID();
    private static final String KAFKA_URL_LOCAL = "127.0.0.1:9091";
    private static final String KAFKA_URL = "http://KafkaAndFlinkExampleKafka:9092";

    private FlinkProperties() {
    }

    public static Properties getPropertiesSource(boolean flinkEnv) {
        Properties properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, getKafkaUrl(flinkEnv));
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID + UUID.randomUUID().toString());
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