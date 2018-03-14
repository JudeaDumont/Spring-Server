package server.springcontext.websockets

import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer

@Configuration
class WebSocketSecurityConfig : AbstractSecurityWebSocketMessageBrokerConfigurer() {

    override fun configureInbound( messages : MessageSecurityMetadataSourceRegistry) {
        messages
                .nullDestMatcher().authenticated()
        .simpSubscribeDestMatchers("/topic/**").authenticated()
//       hasRole("PERSON")
        .simpDestMatchers("/app/**").authenticated()
//       hasRole("USER")
//       simpTypeMatchers(MESSAGE, SUBSCRIBE).denyAll()
        .anyMessage().denyAll()
    }

    override fun sameOriginDisabled(): Boolean {
        return true
    }
}