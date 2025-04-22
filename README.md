# Kafka + Flink + Kafka Streams Demo

This application demonstrates data processing using Kafka Streams and Flink.
There is a single scenario used for both technologies:

1. A Producer generates data of type User.
2. Kafka Streams and Flink each process this data to create a Client.
3. A Consumer prints the data produced by both Kafka Streams and Flink.

## How to Run

1. **Run the script**

```bash
sh infrastructureSetup/runKafka.sh
```

2. **Verify if Docker containers were created properly**  
   In the terminal, you should see the following output:

```
List topic:

[
   "kc-config",
   "kafka_and_flink_example_output",
   "kafka_and_flink_example_input",
   "kc-status",
   "_schemas",
   "kc-offset"
]
```

3. **Run the Consumer that reads and prints the output**  
   [KafkaConsumerExample.java](src/main/java/pl/pb/kafkaandflinkexample/kafka/KafkaConsumerExample.java)

4. **Run the Kafka Streams or Flink application**  
   Depending on what you want to test:
   - To test **Flink**:  
     [FlinkExample.java](src/main/java/pl/pb/kafkaandflinkexample/flink/FlinkExample.java)
   - To test **Kafka Streams**:  
     [KafkaStreamExample.java](src/main/java/pl/pb/kafkaandflinkexample/kafkastream/KafkaStreamExample.java)

5. **Run the Producer that sends input messages**  
   [KafkaProducerExample.java](src/main/java/pl/pb/kafkaandflinkexample/kafka/KafkaProducerExample.java)

6. **Check the logs to confirm that the message was successfully processed**

---

**Learn and have fun! 🎉**