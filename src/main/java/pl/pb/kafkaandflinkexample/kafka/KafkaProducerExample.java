package pl.pb.kafkaandflinkexample.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import pl.pb.kafkaandflinkexample.config.KafkaFactory;
import pl.pb.kafkaandflinkexample.config.KafkaTopics;

import java.util.Random;

public class KafkaProducerExample {

    public static void main(final String[] args) {

        // create the producer
        final KafkaProducer<JsonNode, JsonNode> producer = KafkaFactory.createKafkaProducer();

        for (; ; ) {
            JsonNode node = new ObjectMapper().createObjectNode().put("id", new Random().nextInt());
            send(KafkaTopics.INPUT_TOPIC, producer, null, node);
            // flush data - synchronous
            producer.flush();
        }


        // flush and close producer
//        producer.close();
    }

    private static void send(final String inputTopic, final KafkaProducer producer, JsonNode key, JsonNode value) {
        try {
            // create a producer record
            final ProducerRecord<byte[], byte[]> producerRecord = new ProducerRecord<>(inputTopic,
                    new ObjectMapper().writeValueAsBytes(key),
                    new ObjectMapper().writeValueAsBytes(value)
            );


            // send data - asynchronous
            producer.send(producerRecord);
            System.out.println("Sent message for 'recordMessages': " + producerRecord);
        } catch (JsonProcessingException e) {


        }
    }
}

