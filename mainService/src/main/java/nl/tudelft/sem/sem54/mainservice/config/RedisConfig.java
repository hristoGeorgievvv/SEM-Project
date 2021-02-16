package nl.tudelft.sem.sem54.mainservice.config;


import nl.tudelft.sem.sem54.mainservice.redis.MessageSubscriber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.GenericToStringSerializer;

@Configuration
@Profile("!ci")
@PropertySource("classpath:application.properties")
public class RedisConfig {

    @Value("${redis.hostname}")
    transient String redisHostname;

    @Value("${redis.password}")
    transient String redisPassword;

    @Autowired
    private MessageSubscriber messageSubscriber;

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
    MessageListenerAdapter messageListenerAdapter() {
        return new MessageListenerAdapter(messageSubscriber);
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
        container.addMessageListener(messageListenerAdapter(), productTopic());
        return container;
    }
}
