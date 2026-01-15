package pl.pb.kafkaandflinkexample.flink;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.connector.kafka.sink.KafkaSink;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import pl.pb.kafkaandflinkexample.config.FlinkFactory;

import static pl.pb.kafkaandflinkexample.config.KafkaTopics.INPUT_TOPIC;
import static pl.pb.kafkaandflinkexample.config.KafkaTopics.OUTPUT_TOPIC;

public class FlinkExampleForRunOnFlinkServer {

    public static void main(String[] args) throws Exception {

        final boolean runOnFlinkServer = true;

        final KafkaSource<JsonNode> kafkaSource = FlinkFactory.buildKafkaSource(INPUT_TOPIC, runOnFlinkServer);
        final KafkaSink<JsonNode> kafkaSink = FlinkFactory.buildKafkaSink(OUTPUT_TOPIC, runOnFlinkServer);

        //Without Flink server
        final StreamExecutionEnvironment streamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment();

        streamExecutionEnvironment.fromSource(kafkaSource, WatermarkStrategy.noWatermarks(), "Kafka Source with Key+Value")
                .map(x -> {
                            System.out.println("message: " + x);
                            return x;
                        }
                )
                .sinkTo(kafkaSink);

        streamExecutionEnvironment.execute("Flink + Kafka + Avro + Schema Registry");
    }

}
