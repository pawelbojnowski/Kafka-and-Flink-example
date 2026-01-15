package pl.pb.kafkaandflinkexample.kafka;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import pl.pb.kafkaandflinkexample.config.KafkaFactory;
import pl.pb.kafkaandflinkexample.config.KafkaTopics;
import pl.pb.kafkamodel.avro.Client;
import pl.pb.kafkamodel.avro.Id;

import java.io.IOException;
import java.time.Duration;
import java.util.List;

public class KafkaConsumerExample {


    public static void main(String[] args) throws Exception {
        // create consumer
        final KafkaConsumer<byte[], byte[]> consumer = KafkaFactory.createKafkaConsumer();

        // add subscribed topic(s)
        consumer.subscribe(List.of(KafkaTopics.INPUT_TOPIC));

        // consume data
        while (true) {
            consumer.poll(Duration.ofMillis(100))
                    .forEach(consumerRecord -> {
                        try {
                            System.out.println(String.format("Topic: %s, Key: %s, Value: %-10s Partition: %s, Offset: %s",
                                    consumerRecord.topic(),
                                    new ObjectMapper().readTree(consumerRecord.key()),
                                    new ObjectMapper().readTree(consumerRecord.value()),
                                    consumerRecord.partition(),
                                    consumerRecord.offset())
                            );
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
        }
    }

}
