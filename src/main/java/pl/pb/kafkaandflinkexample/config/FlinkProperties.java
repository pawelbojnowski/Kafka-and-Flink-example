package pl.pb.kafkaandflinkexample.config;

import java.util.Properties;
import java.util.UUID;

public class FlinkProperties {

    public static final String GROUP_ID = "flink_group_" + UUID.randomUUID();
    private static final String KAFKA_URL_LOCAL = "127.0.0.1:9091";
    private static final String KAFKA_URL = "127.0.0.1:9091";

    private FlinkProperties() {
    }


    public static String getKafkaUrl(boolean flinkEnv) {
        return flinkEnv ? KAFKA_URL : KAFKA_URL_LOCAL;
    }

    public static Properties getPropertiesSink(boolean flinkEnv) {
        Properties properties = new Properties();
        return properties;
    }
}