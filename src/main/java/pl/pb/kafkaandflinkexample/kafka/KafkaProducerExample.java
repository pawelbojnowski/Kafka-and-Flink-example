package pl.pb.kafkaandflinkexample.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import pl.pb.kafkaandflinkexample.config.KafkaFactory;
import pl.pb.kafkaandflinkexample.config.KafkaTopics;
import pl.pb.kafkamodel.avro.Id;
import pl.pb.kafkamodel.avro.User;

import java.util.stream.IntStream;

public class KafkaProducerExample {

    public static void main(final String[] args) {

        // create the producer
        final KafkaProducer<Id, User> producer = KafkaFactory.createKafkaProducer();

        IntStream.range(1, 10).forEach(id -> {
            User client = User.newBuilder()
                    .setId(new Id(id))
                    .setFirstname("Harry")
                    .setLastname("Potter")
                    .setPhoneNumber(400400400)
                    .build();
            send(KafkaTopics.INPUT_TOPIC, producer, client.getId(), client);
        });


        // flush data - synchronous
        producer.flush();

        // flush and close producer
        producer.close();
    }

    private static void send(final String inputTopic, final KafkaProducer producer, final Id key, final User value) {

        // create a producer record
        final ProducerRecord<Id, User> producerRecord = new ProducerRecord<>(inputTopic, key, value);

        // send data - asynchronous
        producer.send(producerRecord);

        System.out.println("Sent message for 'recordMessages': " + producerRecord);
    }
}

