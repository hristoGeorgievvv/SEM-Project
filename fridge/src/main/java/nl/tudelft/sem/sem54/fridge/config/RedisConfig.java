package nl.tudelft.sem.sem54.fridge.config;

import com.google.gson.Gson;
import nl.tudelft.sem.sem54.fridge.redis.RedisMessagePublisher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.GenericToStringSerializer;

@Configuration
@PropertySource("classpath:application.properties")
public class RedisConfig {

    @Value("${redis.hostname}")
    transient String redisHostname;

    @Value("${redis.password}")
    transient String redisPassword;

    @Bean
    JedisConnectionFactory jedisConnectionFactory() {
        RedisStandaloneConfiguration redisStandaloneConfiguration =
                new RedisStandaloneConfiguration(redisHostname, 6379);

        redisStandaloneConfiguration.setPassword(redisPassword);

        return new JedisConnectionFactory(redisStandaloneConfiguration);
    }

    /**
     * Defines our default RedisTemplate since spring-data-redis doesn't provide it.
     *
     * @return Default KV template bean.
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        final RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory());
        template.setValueSerializer(new GenericToStringSerializer<>(Object.class));
        return template;
    }

    @Bean
    RedisMessagePublisher messagePublisher() {
        return new RedisMessagePublisher(redisTemplate(), productTopic());
    }

    @Bean
    ChannelTopic productTopic() {
        return new ChannelTopic("products");
    }

    @Bean
    RedisMessageListenerContainer redisContainer() {
        RedisMessageListenerContainer container
                = new RedisMessageListenerContainer();
        container.setConnectionFactory(jedisConnectionFactory());
        return container;
    }

    @Bean
    Gson gson() {
        return new Gson();
    }


}
