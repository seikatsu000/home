import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import java.util.Properties;

public class MyKafkaProducer {
    private static final String BOOTSTRAP_SERVERS = "localhost:9092"; // Replace with your Kafka broker(s) address

    public static void sendMessageToKafka(String key, String message) {
        Properties props = new Properties();
        props.put("bootstrap.servers", BOOTSTRAP_SERVERS);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
                System.out.println("Sending Message: " + message);
        try (Producer<String, String> producer = new KafkaProducer<>(props)) {
            String topic = "my-topic";

            ProducerRecord<String, String> record = new ProducerRecord<>(topic, key, message);

            producer.send(record);
	    System.out.println(" Message Sent  ");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

