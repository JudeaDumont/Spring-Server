package server.springcontext.testmodules

import server.springcontext.datadefinition.DataDefinition
import server.springcontext.datadefinition.ModelDefinition

//Gets dynamically called from sessions.kt
//App module constructor takes a callback that gets called when the app process raw data into high level properties
//App module is not passed raw properties, it knows the channel for which its raw properties come in to be processed into the app specific module
//Delta Engine needs to be a generic class that anything can use
//delta engine goes inside each app module, deltas on raw props, and on computed props that go out
class APPModule(
        appName:String

)
//todo: if there is no mapping of raw to abstract, that means the raw property is the abstract property
    : Module {

    override fun unload() {
    }

    //todo:should a module contain its data definition or should it be pulled in from an external file?
    //Device discovery discovers application, instantiates it.
    //part of instantiating the device is that its data definition gets instantiated
    var converter: Converter? = null
    var channelDef: ChannelDefinition? = null
    var dataDef: DataDefinition? = null
    var modelDef: ModelDefinition? = null
    var modelInterests = HashMap<String, String>()
    var msgTypeToPropertyNames = HashMap<String, String>()

    //todo: reading in the configuration file should give us the dataschema and the channels an app contains
    //todo: potentially both should be available

    override fun getDataDefinition(): DataDefinition {
        return dataDef!!
    }

    override fun getModelDefinition(): ModelDefinition {
        return modelDef!!
    }

    fun addPropLevelInterests(propLevelInterests: HashMap<String, String>) {
        modelInterests.putAll(propLevelInterests)
    }

    fun addPropLevelInterests(propLevelInterests: ArrayList<String>) {
        for (propLevelInterest in propLevelInterests) {
            modelInterests.put(propLevelInterest, "")
        }
    }

    fun removePropLevelInterests(propLevelInterests: HashMap<String, String>) {
        for (propLevelInterest in propLevelInterests) {
            modelInterests.remove(propLevelInterest.key)
        }
    }

    fun addMsgLevelInterests() {
        //Add all property names that are related to that message type

    }

    fun removeMsgLevelInterests() {

    }

    fun setApp(
            channelDef: String,
            channelReqDefMsg: String,
            dataDef: String,
            dataReqDefMsg: String,
            requestChannel: String,
            converter: Converter) {
        //todo:Ensure that ALL app specific logic/naming/etc is only contained in that apps module
        println("APPMODULE!!!!")
        this.converter = converter
        this.channelDef = ChannelDefinition(requestChannel, channelReqDefMsg)
        this.dataDef = DataDefinition(requestChannel, dataReqDefMsg)
        //        "APP.inChannel", "requestChannelSchema"
        //        "APP.inChannel", "requestDataSchema"
        modelDef = ModelDefinition()
    }
}

