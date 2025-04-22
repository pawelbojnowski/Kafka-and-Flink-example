package pl.pb.kafkaandflinkexample.kafka;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import pl.pb.kafkaandflinkexample.config.KafkaFactory;
import pl.pb.kafkaandflinkexample.config.KafkaTopics;
import pl.pb.kafkamodel.avro.Client;
import pl.pb.kafkamodel.avro.Id;

import java.time.Duration;
import java.util.List;

public class KafkaConsumerExample {


    public static void main(String[] args) throws Exception {
        // create consumer
        final KafkaConsumer<Id, Client> consumer = KafkaFactory.createKafkaConsumer();

        // add subscribed topic(s)
        consumer.subscribe(List.of(KafkaTopics.OUTPUT_TOPIC));

        // consume data
        while (true) {
            consumer.poll(Duration.ofMillis(100))
                    .forEach(consumerRecord -> System.out.println(String.format("Topic: %s, Key: %s, Value: %-10s Partition: %s, Offset: %s",
                            consumerRecord.topic(),
                            consumerRecord.key(),
                            consumerRecord.value(),
                            consumerRecord.partition(),
                            consumerRecord.offset())
                    ));
        }
    }

}
