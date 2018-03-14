package server.springcontext.channels

import server.springcontext.redis.RedisPubSubber
//Manages server only channels
object ServerChannelManager {
    var serverOnlyChannels = HashMap<String, ServerOnlyChannelMethodI>()

    fun serverOnlyChannel(channel: String): Boolean {
        println("channel" + channel)
        return serverOnlyChannels.contains(channel)
    }

    //todo: another message stopping error would be that there are no apps up

    fun processServerOnlyChannel(channel: String, message: String) {
        if (serverOnlyChannels.containsKey(channel)) {
            println("processServerOnlyChannel" + message)
            serverOnlyChannels.get(channel)!!.run(message)
        }
    }

    fun addServerOnlyChannels(channels: HashMap<String, ServerOnlyChannelMethodI>) {
        serverOnlyChannels.putAll(channels)
    }
}