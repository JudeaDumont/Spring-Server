package server.springcontext.interestsmechanism

import server.springcontext.interestsmechanism.clientmodelinterests.ClientModelInterests
import server.springcontext.utilities.DataStructureConverter
import java.util.HashMap

//todo:would take a class of the model interests a client has
class ClientRawInterestTree(clientModelInterests: ClientModelInterests, sessionID:String) {
//    High level props and high level interests only

    //each app name contains all of these hashmaps
    var appInterests = HashMap<String, HashMap<String, HashMap<String, String>>>()
    var clientModelInterests : ClientModelInterests?
    var sessionID:String?

    init {
        this.sessionID = sessionID
        this.clientModelInterests = clientModelInterests
        var interestTree = clientModelInterests.apps
//        this.categoriesWhole = JsonUtilities.hashMapFromJsonArray(interestTree)
//        this.categoriesAll = HashMap(this.categoriesWhole)
//        this.propNames = JsonUtilities.hashMapFromJsonArray(incomingPropNames)

//        println(interestTree)
//        println("PropNames"+this.propNames)

        //todo: split up the implementation of converting client interests into raw data interests into another class
        //todo: the clientInterests class should only have to do with directly related high level client interests.

        for (appNameEl in interestTree) {

            val appNameSimple = appNameEl.key
            var appClassName = appNameSimple
            var appinterest = HashMap<String, HashMap<String, String>>()
            val categories = appNameEl.value.cats
            val props = appNameEl.value.props
            val options = appNameEl.value.options

            var categoriesWholeHashMap = HashMap<String, String>()

            for (category in categories) {
                categoriesWholeHashMap.put(appNameSimple + "." + category, "")
            }

            var categoriesViaProps = HashMap<String, String>()
            var categoriesAllHashMap = HashMap(categoriesWholeHashMap)
            var propNamesHashMap = DataStructureConverter.arrayToHashMap(props)
            var optionsHashMap = DataStructureConverter.arrayToHashMap(options)

            for (propName in propNamesHashMap) {
                //this is to see what raw messages a client is interested in

                val dataDefinition = ModuleLoader.moduleFullNameToModuleInstance.get(appClassName)!!.getDataDefinition()
                val modelDefinition = ModuleLoader.moduleFullNameToModuleInstance.get(appClassName)!!.getModelDefinition()

                //need abstract properties to test this.
                val rawProps = modelDefinition.abPropToRawProps.get(propName.key)

                var rawMsgs = HashMap<String, String>()

                if (rawProps != null) {
                    for (rawProp in rawProps) {
                        rawMsgs.putIfAbsent(dataDefinition.rawPropertyToMessage.get(rawProp.key)!!, "")
                    }
                }

                for (rawMsg in rawMsgs) {
                    if (!categoriesWholeHashMap.containsKey(rawMsg.key)) {
                        categoriesViaProps.putIfAbsent(rawMsg.key, "")
                    }
                }
            }

            categoriesAllHashMap.putAll(categoriesViaProps)

            appinterest.put("categoriesViaProps", categoriesViaProps)
            appinterest.put("categoriesWhole", categoriesWholeHashMap)
            appinterest.put("categoriesAll", categoriesAllHashMap)
            appinterest.put("propNames", propNamesHashMap)
            appinterest.put("options", optionsHashMap)
            this.appInterests.put(appClassName, appinterest)
        }
    }
}

//To get all of the raw category interests,
//for each appname
//resolve abMsg into abprops
//resolve all abprops into raw messages
//resolve all raw messages into topicNames