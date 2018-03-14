package server.springcontext.testmodules

import server.springcontext.channels.*
import server.springcontext.redis.RedisInterfaces
import server.springcontext.staticinformation.ErrorInformation
import server.springcontext.systemstate.ServerState
import server.springcontext.utilities.JSON


//todo:data definition and channel definition have a lot of duplicated code
//todo:there are lots of code duplicates that could be moved to utility classes
class ChannelDefinition//todo: if the direct dependency can be passed in through a parameter it should be
(channel: String?, requestMessage: String?) {
    var interestNameToChannelName = HashMap<String, String>()
    private var requestDefinitionChannel = ""
    private var requestDefinitionMessage = ""
    val appName: String

    private fun setDefinitionRequestPublishAction(serverOnlyChannels: HashMap<String, ServerOnlyChannelMethodI>, requestDefinitionMessage: String?) {
        serverOnlyChannels.put(RoundTripMap.get(requestDefinitionMessage!!)!!, object : ServerOnlyChannelMethodI {
            override fun run(jsonString: String) {
                setSchema(jsonString)
            }
        })
    }

    //todo:layer of indirection between message interest and channel name

    private fun setSchema(jsonString: String) {
        val channelSchema = JSON.parse(jsonString).entrySet()
        for (applicationLevelEntry in channelSchema) {
            println("applicationLevelEntry.key" + applicationLevelEntry.key)
            var messageName = applicationLevelEntry.value.asString
            if (messageName == null || messageName.equals("")) {
                messageName = applicationLevelEntry.key
            }
            interestNameToChannelName.put(applicationLevelEntry.key, messageName)
            //todo:name the iterator elements after the level of stuff that's being processed
        }
        println("channelSchema:" + interestNameToChannelName)
    }


    fun definitionExists(): Boolean {
        //todo: implement this like the data definition error putter
        if (interestNameToChannelName.isEmpty()) {
            //If no definition existed before the server could be in an error state
            ServerState.removeServerState(ErrorInformation.getChannelDefinitionUndefinedErrorKey())
        } else {
            //If no definition exists then request one
            RedisInterfaces.redisMessager!!.convertAndSend(requestDefinitionChannel, requestDefinitionMessage)

            val categories = ArrayList<String>()
            println("ChanDef" + appName + ".appErrors")
            categories.add(appName + ".appErrors")
            ServerState.addServerState(
                    ErrorInformation.getChannelDefinitionUndefinedErrorKey(),
                    ErrorInformation.getChannelDefinitionError(requestDefinitionChannel),
                    categories
            )
        }
        return !interestNameToChannelName.isEmpty()
    }

    init {
        appName = requestDefinitionChannel.split(".")[0]
        var inboundChannel = appName + "." + requestDefinitionMessage
        RoundTripMap.map(requestMessage!!, "APP.requestChannelSchema")
        this.requestDefinitionChannel = channel!!
        this.requestDefinitionMessage = requestMessage
        val serverOnlyChannels = HashMap<String, ServerOnlyChannelMethodI>()
        setDefinitionRequestPublishAction(serverOnlyChannels, requestDefinitionMessage)
        ServerChannelManager.addServerOnlyChannels(serverOnlyChannels)
        RedisInterfaces.redisMessager!!.convertAndSend(requestDefinitionChannel, requestDefinitionMessage)
        addMessageStoppingErrors(appName)
    }

    private fun addMessageStoppingErrors(appName: String) {
        var messageStoppingErrors = HashMap<String, MessageStoppingErrorMethod>()
        messageStoppingErrors.put(
                ErrorInformation.getChannelDefinitionUndefinedErrorKey(),
                object : MessageStoppingErrorMethod {
                    override fun run(): Boolean {
                        return !definitionExists()
                    }
                }
        )
        ChannelErrorManager.appNameToErrors.put(appName, messageStoppingErrors)
    }
}