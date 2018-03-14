package server.springcontext.websockets.stompevents

import org.springframework.context.ApplicationListener
import org.springframework.context.annotation.Configuration
import org.springframework.messaging.simp.stomp.StompHeaderAccessor
import org.springframework.web.socket.messaging.SessionConnectEvent
import server.springcontext.interestsmechanism.ClientRawInterestTree
import server.springcontext.interestsmechanism.InterestManager
import server.springcontext.interestsmechanism.ModuleLoader
import server.springcontext.interestsmechanism.ServerInterests
import server.springcontext.interestsmechanism.clientmodelinterests.ClientModelInterests
import server.springcontext.utilities.JSON

@Configuration
class StompConnectEvent : ApplicationListener<SessionConnectEvent> {

    override fun onApplicationEvent(event: SessionConnectEvent) {
        val sha = StompHeaderAccessor.wrap(event.message)

        val newClientInterests = sha.getNativeHeader("interests")
        //For sending initialization object full state as well as keeping
        //track of who cares about what, so when no one is listening to a particular
        //category in the future, the server stops listening to that category altogether

        //The call to push the full state of interests to a new client should be here.
        //Delta logic needs to be completed first though.
        println("CONNECTEVENT")
        println("newClientInterests.get(0)" + newClientInterests.get(0))

        val clientRawInterestTree =
                ClientRawInterestTree((JSON.parse(newClientInterests.get(0), ClientModelInterests::class.java) as ClientModelInterests),JSON.stringify(sha.sessionId))
        InterestManager.stageInterestTree(clientRawInterestTree)
        ModuleLoader.load(clientRawInterestTree.appInterests.keys, clientRawInterestTree.clientModelInterests!!.channelName)
        //This is for more directly telling the server what it cares about,
        //rather than what it doesnt care about

    }
}