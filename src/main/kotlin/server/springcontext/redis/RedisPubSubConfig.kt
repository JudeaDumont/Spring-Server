package server.springcontext.redis

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter
import java.util.concurrent.CountDownLatch

@Configuration
class RedisPubSubConfig{
    @Bean
    internal fun listenerAdapter(receiver: RedisInterfaces): MessageListenerAdapter {
        return MessageListenerAdapter(receiver, "receiveMessage")
    }

    @Bean
    internal fun receiver(latch: CountDownLatch): RedisInterfaces {
        return RedisInterfaces(latch)
    }
}
