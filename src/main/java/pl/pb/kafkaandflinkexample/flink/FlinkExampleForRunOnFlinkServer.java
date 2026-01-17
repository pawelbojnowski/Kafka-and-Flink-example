package pl.pb.kafkaandflinkexample.flink;

import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.connector.kafka.sink.KafkaSink;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import pl.pb.kafkaandflinkexample.config.FlinkFactory;
import pl.pb.kafkaandflinkexample.config.FlinkProperties;
import pl.pb.kafkamodel.avro.Client;
import pl.pb.kafkamodel.avro.Id;
import pl.pb.kafkamodel.avro.User;

import java.util.UUID;

import static pl.pb.kafkaandflinkexample.config.KafkaTopics.INPUT_TOPIC;
import static pl.pb.kafkaandflinkexample.config.KafkaTopics.OUTPUT_TOPIC;

public class FlinkExampleForRunOnFlinkServer {

    public static void main(String[] args) throws Exception {
        final boolean runOnFlinkServer = true;
        final KafkaSource<Tuple2<Id, User>> kafkaSource = FlinkFactory.buildKafkaSource(INPUT_TOPIC, Id.class, User.class, runOnFlinkServer);
        final KafkaSink<Tuple2<Id, Client>> kafkaSink = FlinkFactory.buildKafkaSink(OUTPUT_TOPIC, Id.class, Client.class, runOnFlinkServer);
        final TypeHint<Tuple2<Id, Client>> typeHint = new TypeHint<>() {
        };

        //Without Flink server
        final StreamExecutionEnvironment streamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment();

        streamExecutionEnvironment.fromSource(kafkaSource, WatermarkStrategy.noWatermarks(), "Kafka Source with Key+Value")
                .map(FlinkExampleForRunOnFlinkServer::convertToClient)
                .returns(typeHint)
                .sinkTo(kafkaSink);

        streamExecutionEnvironment.execute("Flink + Kafka + Avro + Schema Registry");
    }

    private static Tuple2<Id, Client> convertToClient(Tuple2<Id, User> idUserTuple2) {
        final User user = idUserTuple2.f1;
        final Client client = new Client(UUID.randomUUID().toString(), user.getId(), user.getFirstname(), user.getLastname(), user.getPhoneNumber());
        System.out.println("Client: " + client.toString());
        return Tuple2.of(idUserTuple2.f0, client);
    }
}
