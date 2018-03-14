package server.springcontext.internalmechanisms

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.concurrent.CountDownLatch

@Configuration
class SynchronizationLatchConfig{
    //This is actually a requirement of the system to have some sort of synchronization latch
    @Bean
    internal fun latch(): CountDownLatch {
        return CountDownLatch(1)
    }
}