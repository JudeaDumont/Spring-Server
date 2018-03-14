package server.springcontext.datadefinition

import server.springcontext.channels.*
import server.springcontext.interestsmechanism.ModuleLoader
import server.springcontext.staticinformation.ErrorInformation
import server.springcontext.redis.RedisInterfaces
import server.springcontext.systemstate.ServerState
import server.springcontext.utilities.JSON

//Used to provide modules with a class that houses the raw data props
//The class is also responsible for roundtripping its initialization configuration
//rather than having it passed in.

//todo: use a class for parsing out the data definition

//todo: schema should be per module
//todo:but this generic processor is fine. each schema should have this format:

//todo: in the future, device discovery will instantiate one of these per application
//todo: it will request datadefinitions from a dynamic location that would be set on instantiating the object

class DataDefinition {
    //todo:Data def still needs to be pulled in and/or used appropriately
    var dataDefinition: DataSchema? = null
    var rawPropertyToMessage = HashMap<String, String>()
    var requestDefinitionChannel = "" //"APP.inChannel"
    var requestDefinitionMessage = "" //requestDefinition
    val appName: String

    constructor(requestDefinitionChannel: String?, requestDefinitionMessage: String?) {
        appName = requestDefinitionChannel!!.split(".")[0]
        var inboundChannel = appName + "." + requestDefinitionMessage
        RoundTripMap.map(requestDefinitionMessage!!, inboundChannel)
        println("inboundChannel" + inboundChannel)
        this.requestDefinitionChannel = requestDefinitionChannel
        this.requestDefinitionMessage = requestDefinitionMessage
        val serverOnlyChannels = HashMap<String, ServerOnlyChannelMethodI>()
        setDefinitionRequestPublishAction(serverOnlyChannels, requestDefinitionMessage)
        ServerChannelManager.addServerOnlyChannels(serverOnlyChannels)
        RedisInterfaces.redisMessager!!.convertAndSend(requestDefinitionChannel, requestDefinitionMessage)
        //Part of a classes responsibility is registering message stopping errors
        println("DataDefinitionAppName" + appName)
        addMessageStoppingErrors(appName)
    }

    private fun addMessageStoppingErrors(appName: String) {
        var messageStoppingErrors = HashMap<String, MessageStoppingErrorMethod>()
        messageStoppingErrors.put(
                ErrorInformation.getDataDefinitionUndefinedErrorKey(),
                object : MessageStoppingErrorMethod {
                    override fun run(): Boolean {
                        return !definitionExists()
                    }
                }
        )
        ChannelErrorManager.appNameToErrors.put(appName, messageStoppingErrors)
    }

    private fun setDefinitionRequestPublishAction(serverOnlyChannels: HashMap<String, ServerOnlyChannelMethodI>, requestDefinitionMessage: String?) {
        serverOnlyChannels.put(RoundTripMap.get(requestDefinitionMessage!!)!!, object : ServerOnlyChannelMethodI {
            override fun run(jsonString: String) {
                setSchema(jsonString)
                ModuleLoader.dataDefinitionSetAction(appName)
            }
        })
    }

    fun definitionExists(): Boolean {
        if (dataDefinition != null) {
            //If no definition existed before the server could be in an error state
            ServerState.removeServerState(ErrorInformation.getDataDefinitionUndefinedErrorKey())
        } else {
            //If no definition exists then request one
            RedisInterfaces.redisMessager!!.convertAndSend(requestDefinitionChannel, requestDefinitionMessage)
            val categories = ArrayList<String>()
            categories.add(appName + ".appErrors")
            //todo: If there is no module, an interest should be added for the client to let them know the module they want to load is not there

            println("DataDef" + appName + ".appErrors")
            ServerState.addServerState(
                    ErrorInformation.getDataDefinitionUndefinedErrorKey(),
                    ErrorInformation.getDataDefinitionError(requestDefinitionChannel.split(".")[0]),
                    categories
            )
        }
        return dataDefinition != null
    }

    fun setSchema(jsonString: String) {
        println("setDataSchema: " + jsonString)
        val parse = JSON.parse(jsonString, DataSchema::class.java) as DataSchema
        dataDefinition = parse
        println("dataSchema:" + parse)
    }
}
