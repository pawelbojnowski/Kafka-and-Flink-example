package pl.pb.kafkaandflinkexample.kafkastream;

import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.ForeachAction;
import org.apache.kafka.streams.kstream.Produced;
import pl.pb.kafkaandflinkexample.config.KafkaFactory;
import pl.pb.kafkamodel.avro.Client;
import pl.pb.kafkamodel.avro.Id;
import pl.pb.kafkamodel.avro.User;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;

import static pl.pb.kafkaandflinkexample.config.KafkaSerde.KEY_SERDE_ID;
import static pl.pb.kafkaandflinkexample.config.KafkaSerde.VALUE_SERDE_CLIENT;
import static pl.pb.kafkaandflinkexample.config.KafkaSerde.VALUE_SERDE_USER;
import static pl.pb.kafkaandflinkexample.config.KafkaTopics.INPUT_TOPIC;
import static pl.pb.kafkaandflinkexample.config.KafkaTopics.OUTPUT_TOPIC;

public class KafkaStreamExample {

    public static final Consumed<Id, User> CONSUMED = Consumed.with(KEY_SERDE_ID, VALUE_SERDE_USER);
    public static final Produced<Id, Client> PRODUCED = Produced.with(KEY_SERDE_ID, VALUE_SERDE_CLIENT);

    public static void main(final String[] args) {

        final StreamsBuilder builder = new StreamsBuilder();

        createStream(builder);

        final KafkaStreams streams = KafkaFactory.createKafkaStreams(builder.build());

        closeKafkaStreams(streams);
    }

    static void createStream(final StreamsBuilder builder) {
        builder.stream(INPUT_TOPIC, CONSUMED)
                .peek(print())
                .mapValues(value -> buildClient(value))
                .peek(print())
                .to(OUTPUT_TOPIC, PRODUCED);
    }

    private static ForeachAction<Id, Object> print() {
        return (key, value) -> System.out.println(key + " = " + value);
    }

    private static Client buildClient(User value) {
        return Client.newBuilder()
                .setId(value.getId())
                .setUserId(UUID.randomUUID().toString())
                .setFirstname(value.getFirstname())
                .setLastname(value.getLastname())
                .setPhoneNumber(value.getPhoneNumber())
                .build();
    }


    public static void closeKafkaStreams(final KafkaStreams streams) {
        final CountDownLatch latch = new CountDownLatch(1);
        Runtime.getRuntime().addShutdownHook(new Thread("kafka-stream-example-shutdown-hook") {
            @Override
            public void run() {
                streams.close();
                latch.countDown();
            }
        });

        try {
            streams.start();
            latch.await();
        } catch (final Exception e) {
            System.exit(1);
        }
        System.exit(0);
    }

}
