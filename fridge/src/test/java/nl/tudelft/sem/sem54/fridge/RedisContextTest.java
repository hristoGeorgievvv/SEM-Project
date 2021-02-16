package nl.tudelft.sem.sem54.fridge;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.context.SpringBootTest;
import redis.embedded.RedisServerBuilder;

@SpringBootTest
public class RedisContextTest {

    private static redis.embedded.RedisServer redisServer;

    @BeforeAll
    public static void startRedisServer() {
        redisServer = new RedisServerBuilder().port(6379).setting("maxmemory 256M").build();
        redisServer.start();
    }

    @AfterAll
    public static void stopRedisServer() {
        redisServer.stop();
    }
}