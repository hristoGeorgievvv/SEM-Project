package nl.tudelft.sem.sem54.mainservice.redis;


import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

@Service
public class MessageSubscriber implements MessageListener {

    private Adapter adapter;

    public MessageSubscriber(RedisAdapter adapter) {
        this.adapter = adapter;
    }

    /**
     * Gets called whenever redis receives a message on a certain channel.
     * This is a handler for tracking product expirations, completions and removals.
     *
     * @param message Message received
     * @param pattern Message pattern mask - redis thing we won't use
     */
    public void onMessage(final Message message, final byte[] pattern) {
        String messageText = new String(message.getBody());
        // TODO Replace println's with a propper logging framework
        System.out.println("Message received: " + messageText);

        adapter.process(messageText);
    }
}
