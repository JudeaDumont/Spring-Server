package server.springcontext.channels

import server.springcontext.redis.RedisPubSubber

object RoundTripMap{

    private var map = HashMap<String, String>()
    fun map(outBoundToRedis: String, inBound: String) {
        RedisPubSubber.addSub(inBound)
        map.put(outBoundToRedis,inBound)
    }

    fun get(requestDefinitionMessage: String): String? {
        return map.get(requestDefinitionMessage)
    }
}