package nl.tudelft.sem.sem54.mainservice.redis;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.connection.DefaultMessage;
import org.springframework.data.redis.connection.Message;


@SpringBootTest
class MessageSubscriberTest {

    @MockBean
    RedisAdapter redisAdapter;

    @Autowired
    MessageSubscriber messageSubscriber;

    @Test
    void testOnMessage() {
        String json = "{}";

        Message message = new DefaultMessage("test".getBytes(), json.getBytes());

        messageSubscriber.onMessage(message, new byte[0]);

        verify(redisAdapter, times(1)).process(eq(json));
    }
}