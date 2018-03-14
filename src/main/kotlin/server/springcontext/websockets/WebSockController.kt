package server.springcontext.websockets

import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.stereotype.Controller
import server.springcontext.interestsmechanism.InterestsChangeRequestObject
import server.springcontext.interestsmechanism.InterestManager
import server.springcontext.utilities.JSON

@Controller
class WebSockController {
    @MessageMapping("/server")
    @Throws(Exception::class)
    fun changeInterest(recievedObject: InterestsChangeRequestObject): String {
        val channelName = recievedObject.channelName
        val category = recievedObject.name
        if(category!!.contains("R=")!!){
            val droppedCat = category.split("R=")[1]
            InterestManager.removeTopic(channelName!!,droppedCat)
        }
        if(category.contains("A=")){
            val addedCategory = category.split("A=")[1]
            InterestManager.addTopic(addedCategory, channelName!!)
        }
        return (JSON.stringify(recievedObject))
    }
}