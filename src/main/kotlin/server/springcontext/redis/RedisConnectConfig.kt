package server.springcontext.redis

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.core.StringRedisTemplate

@Configuration
class RedisConnectConfig{

    @Bean
    internal fun template(connectionFactory: RedisConnectionFactory): StringRedisTemplate {
        val stringRedisTemplate = StringRedisTemplate(connectionFactory)
        RedisInterfaces.redisMessager = stringRedisTemplate
        return stringRedisTemplate
    }
}