package server.springcontext.testmodules

import server.springcontext.datadefinition.DataDefinition
import server.springcontext.datadefinition.ModelDefinition

class TFEModule : Module{
    override fun getModelDefinition(): ModelDefinition {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun unload() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getDataDefinition(): DataDefinition {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    var modelInterests = HashMap<String,String>()
    var msgTypeToPropertyNames = HashMap<String,String>()
    fun addPropLevelInterests(propLevelInterests:HashMap<String,String>){
        modelInterests.putAll(propLevelInterests)
    }
    fun addPropLevelInterests(propLevelInterests:ArrayList<String>){
        for (propLevelInterest in propLevelInterests) {
            modelInterests.put(propLevelInterest,"")
        }
    }
    fun removePropLevelInterests(propLevelInterests:HashMap<String,String>){
        for (propLevelInterest in propLevelInterests) {
            modelInterests.remove(propLevelInterest.key)
        }
    }
    fun addMsgLevelInterests(){
        //Add all property names that are related to that message type

    }
    fun removeMsgLevelInterests(){

    }
}
