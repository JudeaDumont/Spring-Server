package server.springcontext.redis

import org.springframework.data.redis.listener.PatternTopic
import org.springframework.data.redis.listener.RedisMessageListenerContainer
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter

object RedisPubSubber {

    var redisContainer: RedisMessageListenerContainer? = null
    var redisListenerAdapter: MessageListenerAdapter? = null
    var numOfSubscriptions = 0
    var subbedChannels = HashMap<String,String>()
    fun addSub(topic: String) {
        if (redisListenerAdapter != null) {
            addChannelUnderAppName(topic)
        }
    }

    private fun addChannelUnderAppName(topic: String) {

        val topicSplit = topic.split(".")
        var restOfTopic = ""
        for (i in 1 until topicSplit.size) {
            restOfTopic += topicSplit[i]
            if (i + 1 < topicSplit.size) {
                restOfTopic += "."
            }
        }
        println("interestNameToChannelName" + topicSplit[0] + " " + restOfTopic)
        if(!subbedChannels.contains(topic)){
            listen(topic)
            subbedChannels.put(topic, "")
            //todo: everywhere a function is called and the first thing that function does is check to see if its valid,
            //that function call should be wrapped instead, this avoids unneccessary function call overhead.
        }
    }

    private fun listen(topic: String) {
        redisContainer!!.addMessageListener(redisListenerAdapter, PatternTopic(topic))
        ++numOfSubscriptions
        println(numOfSubscriptions)
        println("topicSub: " + topic)
    }

    fun removeSub(topic: String) {
        if (redisListenerAdapter != null) {
            redisContainer!!.removeMessageListener(redisListenerAdapter, PatternTopic(topic))
            --numOfSubscriptions
//                println(numOfSubscriptions)
            println(numOfSubscriptions)
            println("topicUnSub: " + topic)
            if(subbedChannels.contains(topic)){
                subbedChannels.remove(topic)
                //todo: everywhere a function is called and the first thing that function does is check to see if its valid,
                //that function call should be wrapped instead, this avoids unneccessary function call overhead.
            }
        }
    }
}