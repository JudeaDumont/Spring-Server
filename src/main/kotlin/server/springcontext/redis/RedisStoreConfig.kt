package server.springcontext.redis

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.listener.PatternTopic
import org.springframework.data.redis.listener.RedisMessageListenerContainer
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter

@Configuration
class RedisStoreConfig {
    //Configure listeners and set redis pubsubber for dynamic subscription/unsubscription
    @Bean
    internal fun container(connectionFactory: RedisConnectionFactory,
                           listenerAdapter: MessageListenerAdapter): RedisMessageListenerContainer {
        val container = RedisMessageListenerContainer()
        container.connectionFactory = connectionFactory
        //todo: the CCT app should add this message listener
        RedisPubSubber.redisContainer = container
        RedisPubSubber.redisListenerAdapter = listenerAdapter

        return container
    }

}