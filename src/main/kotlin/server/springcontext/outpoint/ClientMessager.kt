package server.springcontext.outpoint

import org.springframework.messaging.simp.SimpMessagingTemplate
import server.springcontext.interestsmechanism.InterestManager
import server.springcontext.systemstate.SystemDeltas
import server.springcontext.systemstate.SystemStateStore
import server.springcontext.utilities.JSON

class ClientMessager {

    companion object {
        @JvmStatic
        fun beginMessagingFiltration(category: String, message: String, template: SimpMessagingTemplate?) {
            SystemStateStore.updateFullState(category,message)
            //Whole Category interests go first
            val checkSessionInterests = InterestManager.checkSessionInterestsByWholeCategory(category)
            if (checkSessionInterests != null) {
                for (clientChannel in checkSessionInterests) {
    //                println("DEBUG:"+SystemDeltas.deltasList)
                    template!!.convertAndSend("/topic/" + clientChannel, JSON.stringify(SystemDeltas.deltasList.get(category)!!))
                }
            }
            else{
                println("Was Expecting Interests But there were none")
            }
        }
    }
}