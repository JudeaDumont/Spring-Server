package server.springcontext

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class Application {
    companion object {
        @Throws(InterruptedException::class)
        @JvmStatic
        fun main(args: Array<String>) {
            val ctx = SpringApplication.run(Application::class.java, *args)
        }
    }
}