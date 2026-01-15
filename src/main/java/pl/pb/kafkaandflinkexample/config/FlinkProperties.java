package pl.pb.kafkaandflinkexample.config;

import java.util.Properties;
import java.util.UUID;

public class FlinkProperties {

    public static final String GROUP_ID = "flink_group_" + UUID.randomUUID();
    public static final String KAFKA_URL_LOCAL = "localhost:9091";

    private FlinkProperties() {
    }
 

    public static Properties getPropertiesSink(boolean flinkEnv) {
        Properties properties = new Properties();
        return properties;
    }
}