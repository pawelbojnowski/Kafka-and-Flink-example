package pl.pb.kafkaandflinkexample.config;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.Topology;

public class KafkaFactory {

    private KafkaFactory() {
    }

    public static KafkaStreams createKafkaStreams(Topology kafkaStreams) {
        return new KafkaStreams(kafkaStreams, KafkaProperties.getStreamsConfig());
    }

    public static <K, V> KafkaConsumer<K, V> createKafkaConsumer() {
        return new KafkaConsumer<K, V>(KafkaProperties.getConsumerConfig());
    }

    public static <K, V> KafkaProducer<K, V> createKafkaProducer() {
        return new KafkaProducer<K, V>(KafkaProperties.getProducerConfig());
    }

}
