package server.springcontext.interestsmechanism

import com.google.gson.JsonObject
import server.springcontext.redis.RedisPubSubber
import server.springcontext.utilities.JSON
import java.util.HashMap

object ServerInterests {
    //Name of category interested in, // doesnt matter
    var serverInterests = HashMap<String, String>()
    var interestsObject = JsonObject()

    fun updateServerInterestsBySessionId(droppedSessionId: String?): Boolean {
        //That whole sessions interests need to be checked if the exist elsewhere in other clients interests
        //this is where the server will potentially stop listening to topics
        //based on the interests found within sessionInterests.
        //if no one is interested, the server becomes uninterested
        return false
    }

    fun updateServerInterestsByTopic(droppedTopic: String?): Boolean {
        serverInterests.remove(droppedTopic)
        RedisPubSubber.removeSub(droppedTopic!!)
        //A topic is not being listened to anymore,
        //But users session has not ended,
        //This will happen FREQUENTLY.
        //If a client drops an interest, the server should check if any other client has that interest
        //if not, the server stops listening to those topics from redis.
        return false
    }

    fun addServerInterests(newClientInterests: HashMap<String, HashMap<String, String>>?) {
        //if the server isn't already interested, it becomes interested
//        println("NewClientInterests:" + newClientInterests)
        for (mutableEntry in newClientInterests!!.get("categoriesAll")!!) {
            val appAndChannelName = mutableEntry.key
            println("appAndChannelName"+appAndChannelName)
            serverInterests.put(appAndChannelName, "")
            RedisPubSubber.addSub(appAndChannelName)
        }
    }

    fun addServerInterest(newClientInterest: String) {
        //if the server isn't already interested, it becomes interested
        if (!serverInterests.containsKey(newClientInterest)) {
            serverInterests[newClientInterest] = ""
            RedisPubSubber.addSub(newClientInterest)
        }
    }
}
