import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;
import org.apache.kafka.clients.consumer.*;
import java.io.IOException;
import java.util.Collections;
import java.util.Properties;

public class MyKafkaConsumer {

    private static KafkaConsumer<String, String> consumer;

    static {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("group.id", "console-consumer-49021");
        props.put("enable.auto.commit", "false");

        consumer = new KafkaConsumer<>(props);
        String topic = "my-topic";

        consumer.subscribe(Collections.singletonList(topic));

        // Set up a shutdown hook to close the consumer gracefully on interruption (Ctrl+C)
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutting down Kafka consumer...");
            consumer.close(); // Close the consumer
        }));

        Thread consumerThread = new Thread(() -> {
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(100);

                for (ConsumerRecord<String, String> record : records) {
                    System.out.println("Send From : " + record.key() + " Received message: " + record.value());
                    String message = "Send From : " + record.key() + " Received message: " + record.value();
                    MyWebSocketServer.sendToAll(message); // Send the message to all WebSocket clients

                    consumer.commitSync();
                }
            }
        });

        consumerThread.start();
    }

        public static void main(String[] args) {
        // Initialize the Kafka consumer
        MyKafkaConsumer consumer = new MyKafkaConsumer();

        // Just a placeholder, you can add any logic you want here
        System.out.println("Kafka consumer is running...");

        // Add a sleep to keep the program running (you can replace this with any other logic)
        try {
            Thread.sleep(Long.MAX_VALUE);
        } catch (InterruptedException e) {
            System.out.println("Kafka consumer interrupted.");
        }
    }
}

