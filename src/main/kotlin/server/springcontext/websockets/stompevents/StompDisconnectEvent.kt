package server.springcontext.websockets.stompevents

import org.springframework.context.ApplicationListener
import org.springframework.context.annotation.Configuration
import org.springframework.messaging.simp.stomp.StompHeaderAccessor
import org.springframework.web.socket.messaging.SessionDisconnectEvent
import server.springcontext.interestsmechanism.InterestManager
import server.springcontext.interestsmechanism.ServerInterests
import server.springcontext.utilities.JSON


@Configuration
class StompDisconnectEvent : ApplicationListener<SessionDisconnectEvent> {

    override fun onApplicationEvent(event: SessionDisconnectEvent) {

        //Interest is lost, and perhaps the server quits listening to data the CCT is pushing
        //Based on remaining client interest.

        val sha = StompHeaderAccessor.wrap(event.message)
        ServerInterests.updateServerInterestsBySessionId(sha.sessionId)

        InterestManager.removeSession(JSON.stringify(sha.sessionId))
    }
}