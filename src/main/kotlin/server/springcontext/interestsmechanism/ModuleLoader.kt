package server.springcontext.interestsmechanism

import server.springcontext.channels.RoundTripMap
import server.springcontext.internalmechanisms.ClassLookup
import server.springcontext.redis.RedisPubSubber
import server.springcontext.staticinformation.ErrorInformation
import server.springcontext.systemstate.ServerState
import server.springcontext.testmodules.Module
import java.util.HashMap
import java.util.function.Consumer

object ModuleLoader {

    //todo:update hashmaps that have no hash values to hashsets?
    var moduleFullNameToModuleInstance = HashMap<String, Module>() //wouldnt want to duplicate modules
    var moduleFullNameToUsers = HashMap<String, HashMap<String, String>>() //wouldnt want to duplicate modules

    fun load(appNames: MutableSet<String>, channelName: String) {
        buildUsersOfModules(appNames, channelName)
        loadAppModules(appNames)
    }

    private fun buildUsersOfModules(moduleSets: MutableSet<String>, channelName: String) {
        moduleSets.forEach(Consumer {
            moduleFullNameToUsers.putIfAbsent(it, HashMap())
            moduleFullNameToUsers.get(it)!!.put(channelName, "")
        })
    }

    private fun loadAppModules(appNames: MutableSet<String>) {
        //todo:could potentially have multiple module names of the same type (CCT1, CCT2),
        //todo:could potentially reference all CCTs, all TFEs, etc.
        //todo:could potentially reference specific types of application combinations (all CCTs, TFE1, K2)
        //todo:could potentially reference all Apps available
        for (appName in appNames) {
            loadModule(appName)
        }
    }

    private fun loadModule(appName: String) {
        if (!moduleFullNameToModuleInstance.containsKey(appName)) {
            println("loadAppModules" + appName)
            //todo: if an app is not up, an exception will be thrown here because the appNames class will not be in the lookup table to be instantiated
            val modulesClass = ClassLookup.lookupTable.get(appName)
            if (modulesClass != null) {
                val newInstance = Class.forName(modulesClass).getConstructor()
                if (newInstance is Module) {
                    moduleFullNameToModuleInstance.put(appName, (newInstance))
                }
            } else {
                val categories = ArrayList<String>()
                categories.add(appName + ".appErrors")
                //todo: If there is no module, an interest should be added for the client to let them know the module they want to load is not there

                println("ModuleAvail" + appName + ".appErrors")
                ServerState.addServerState(
                        ErrorInformation.getModuleUnavailableErrorKey(),
                        ErrorInformation.getModuleUnavailableError(appName),
                        categories
                )
                InterestManager.alertUsersOfAppUnavailability(appName)
            }
        }
    }

    fun removeAppsFromClientInterest(droppedApps: ArrayList<String>, channelName: String) {
        for (droppedApp in droppedApps) {
            checkAndRemoveUnusedAppInterest(channelName, droppedApp)
        }
    }

    fun checkAndRemoveUnusedAppInterest(channelName: String, droppedApp: String) {
        println("moduleFullNameToUsers" + moduleFullNameToUsers)
        moduleFullNameToUsers.get(droppedApp)!!.remove(channelName)
        if (moduleFullNameToUsers.get(droppedApp)!!.isEmpty()) {
            if (moduleFullNameToModuleInstance.contains(droppedApp)) {
                removeRoundTripSubscriptions(droppedApp)
                moduleFullNameToModuleInstance.remove(droppedApp)


                println("Dropped App:" + droppedApp)
            } else {
                println("Was Expecting Dropped App: $droppedApp But Was Not Found")
            }
        }
    }

    private fun removeRoundTripSubscriptions(droppedApp: String) {
        RedisPubSubber.removeSub(
                RoundTripMap.get(
                        moduleFullNameToModuleInstance.get(droppedApp)!!
                                .getDataDefinition().requestDefinitionMessage
                )!!
        )
    }

    fun dataDefinitionSetAction(appName: String) {
        InterestManager.executeNewSessionInterests(appName)
    }
}