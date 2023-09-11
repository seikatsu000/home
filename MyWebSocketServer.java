import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import java.util.Properties;
import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import java.io.IOException;
import jakarta.websocket.Session;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint("/websocket")
public class MyWebSocketServer {
	    private static final Set<Session> sessions = new CopyOnWriteArraySet<>();
//        System.out.println("Starting MyWebSocketServer: ");
    @OnOpen
    public void onOpen(Session session) {
        System.out.println("WebSocket opened: " + session.getId());
	        sessions.add(session);
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println("Message from " + session.getId() + ": " + message);
        
        // Handle the received message here, e.g., send it to Kafka producer
                // Split the incoming message into key and message
        String[] parts = message.split(",");
        if (parts.length == 2) {
            String kafkaKey = parts[0].trim();
            String kafkaMessage = parts[1].trim();
            System.out.println("Message from " + kafkaKey + ": " + kafkaMessage);
            // Pass the key and message to your Kafka producer method
            MyKafkaProducer.sendMessageToKafka(kafkaKey, kafkaMessage);
	    sendToAll(kafkaMessage);
        } else {
            System.err.println("Invalid message format: " + message);
        }
    }

    @OnClose
    public void onClose(Session session) {
        System.out.println("WebSocket closed: " + session.getId());
    }

        public static void sendToAll(String message) {
        synchronized (sessions) {
            for (Session session : sessions) {
                try {
		System.out.println("Message sent " + message );	
                    session.getBasicRemote().sendText(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
	}

    @OnError
    public void onError(Session session, Throwable throwable) {
        System.err.println("WebSocket error: " + session.getId());
        throwable.printStackTrace();
    }
}

