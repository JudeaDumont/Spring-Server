package server.springcontext.redissessioncookiefier

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession
import org.springframework.session.web.context.AbstractHttpSessionApplicationInitializer
import org.springframework.session.web.http.CookieSerializer
import org.springframework.session.web.http.DefaultCookieSerializer

@Configuration
@EnableRedisHttpSession
class SessionAndCookieConfig : AbstractHttpSessionApplicationInitializer(){
    var cookieSerializer: CookieSerializer? = null

    //Making a custom cookie means we would have to tell the load balancer what the cookie is
    @Bean
    fun cookieSerializer(): CookieSerializer {
        val serializer = DefaultCookieSerializer()
        serializer.setCookieName("BRAXTON")
        serializer.setCookiePath("/")
        serializer.setDomainNamePattern("^.+?\\.(\\w+\\.[a-z]+)$")
        cookieSerializer = serializer
        return serializer
    }
}