package server.springcontext.interestsmechanism

import server.springcontext.interestsmechanism.ModuleLoader.removeAppsFromClientInterest
import server.springcontext.interestsmechanism.SessionManager.mapSessionToChannel
import server.springcontext.outpoint.ClientMessager
import server.springcontext.redis.RedisInterfaces
import java.util.HashMap

object InterestManager {
    //SessionId //ClientRawInterestTree
    private var clientChannelToRawInterests = HashMap<String, ClientRawInterestTree>()
    private var appNameToRawInterests = HashMap<String, ArrayList<ClientRawInterestTree>>()
    private var rawInterestCategoriesToClientChannels = HashMap<String, HashMap<String, String>>()
    //Categories that are listened to as a result of propname interests
    private var partialCategoriesToClientChannels = HashMap<String, HashMap<String, String>>()

    fun stageInterestTree(newClientInterests: ClientRawInterestTree) {
        //todo: must stage only the portion of the interest tree that is for that app
        for (appInterest in newClientInterests.appInterests) {
            appNameToRawInterests.putIfAbsent(appInterest.key, ArrayList())
            appNameToRawInterests.get(appInterest.key)!!.add(newClientInterests)
            println("stageInterestTree" + appInterest.key)
        }
    }

    fun executeNewSessionInterests(appName: String) {
        println("executeNewSessionInterestsexecuteNewSessionInterests"+appNameToRawInterests.toString())
        println("executeNewSessionInterestsappName"+appName)
        for (clientRawInterestTree in appNameToRawInterests.get(appName)!!) {
            //part of getting a new session is propagating full state back to the client regarding their interests.
            println("executeNewSessionInterests" + appName)
            val channelName = clientRawInterestTree.clientModelInterests!!.channelName
            clientChannelToRawInterests[channelName] = clientRawInterestTree

            //todo:LEFT OFF Data definition does not get set in time for it to set the server only channel for
            //receiving the definition, the message is propagated through the messaging system and breaks everything due to nesting errors.
            //ANSWER: Its because the error for the definition doesn't get set in time.

            //todo: It shouldnt break anything: Write generic json parsing library functions.

            mapSessionToChannel(clientRawInterestTree.sessionID!!, channelName)

            val appInterests = clientRawInterestTree.appInterests.get(appName)
            buildRawInterestsCatsToInterestedClientChannels(appInterests, channelName)

            //todo:Build up partial categories with propnames, add parameter for propnames here
            buildCategoriesToSubscriptionLists(rawInterestCategoriesToClientChannels.keys, channelName)

            ServerInterests.addServerInterests(appInterests)
        }
    }

    private fun buildRawInterestsCatsToInterestedClientChannels(
            clientRawInterestTree: HashMap<String, HashMap<String, String>>?,
            channelName: String?
    ) {
        val newCategories = clientRawInterestTree!!.get("categoriesWhole")!!
        for (newcategory in newCategories) {
            rawInterestCategoriesToClientChannels.putIfAbsent(newcategory.key, HashMap())
            rawInterestCategoriesToClientChannels.get(newcategory.key)!!.put(channelName!!, "")
        }
        println("rawInterestCategoriesToClientChannels" + rawInterestCategoriesToClientChannels)
        //todo:implement the props stuff, model prop interests etc.
        //todo:appname should precede all of its respective props
//        propNames.putAll(app.value.get("propNames")!!)
        println("rawInterestCategoriesToClientChannels:" + rawInterestCategoriesToClientChannels)
    }


    fun buildCategoriesToSubscriptionLists(categories: MutableSet<String>, channelName: String) {
        //builds a list of clientChannels to push a message to
        for (category in categories) {
            val categoryString = category
            //if props are of a particular category, we would add those categories as well
            rawInterestCategoriesToClientChannels.putIfAbsent(categoryString, HashMap())
            rawInterestCategoriesToClientChannels.get(categoryString)!!.put(channelName, "")
        }
        //todo:Build up partial categories with propnames
    }

    fun buildCategoryToSubscriptionList(category: String, channelName: String) {
        //builds a list of clientChannels to push a message to
        //if props are of a particular category, we would add those categories as well
        rawInterestCategoriesToClientChannels.putIfAbsent(category, HashMap())
        rawInterestCategoriesToClientChannels.get(category)!!.put(channelName, "")
    }

    fun removeSession(sessionId: String) {
        val appInterests =
                clientChannelToRawInterests[SessionManager.sessionIdToClientChannel[sessionId]]!!.appInterests

        var droppedApps = ArrayList<String>()
        var droppedCategories = HashMap<String, String>()

        if (appInterests != null) {
            for (appInterest in appInterests) {
                println("appInterest.key" + appInterest.key)
                droppedApps.add(appInterest.key)
                println("appinterest" + appInterest.value)
                droppedCategories.putAll(appInterest.value.get("categoriesAll")!!)
            }
        }

        for (droppedCat in droppedCategories) {
            //rawInterestCategoriesToClientChannels arent setup properly or droppedCategory has wrong app name format
            if (!rawInterestCategoriesToClientChannels.get(droppedCat.key)!!.isEmpty()) {
                rawInterestCategoriesToClientChannels.get(droppedCat.key)!!.remove(SessionManager.sessionIdToClientChannel[sessionId])
                if (rawInterestCategoriesToClientChannels.get(droppedCat.key)!!.isEmpty()) {
                    ServerInterests.updateServerInterestsByTopic(droppedCat.key)
//                    println("Dropped:" + droppedCat.key)
                }
            }
        }

        removeAppsFromClientInterest(droppedApps, SessionManager.sessionIdToClientChannel[sessionId]!!)
        clientChannelToRawInterests.remove(SessionManager.sessionIdToClientChannel[sessionId])
        SessionManager.sessionIdToClientChannel.remove(sessionId)
        //todo:Check to see if app interests needs to be removed serverside (resulting in module unload)
    }


    fun removeTopic(channelName: String, droppedCat: String) {
        val app = droppedCat.split(".")[0]
        val fullChannelName = droppedCat
        if (rawInterestCategoriesToClientChannels.get(fullChannelName) != null
                && !rawInterestCategoriesToClientChannels.get(fullChannelName)!!.isEmpty()) {
            rawInterestCategoriesToClientChannels.get(fullChannelName)!!.remove(channelName)
            if (rawInterestCategoriesToClientChannels.get(fullChannelName)!!.isEmpty()) {
                ServerInterests.updateServerInterestsByTopic(fullChannelName)
                println("Dropped:" + fullChannelName)
            }
            clientChannelToRawInterests[channelName]!!
                    .appInterests.get(app)!!
                    .get("categoriesWhole")!!
                    .remove(fullChannelName)
        }
    }

    fun checkSessionInterestsByWholeCategory(category: String): MutableSet<String>? {
        return rawInterestCategoriesToClientChannels.get(category)?.keys
    }

    fun addTopic(category: String, channelName: String) {
        val app = category.split(".")[0]
        buildCategoryToSubscriptionList(category, channelName)
        clientChannelToRawInterests.get(channelName)!!
                .appInterests.get(app)!!
                .get("categoriesWhole")!!
                .put(category, "")

        ServerInterests.addServerInterest(category)
    }

    fun alertUsersOfAppUnavailability(appName: String) {
        for (appNameToRawInterest in appNameToRawInterests.get(appName)!!) {
            ClientMessager.beginMessagingFiltration(appName+".appErrors", "{}", RedisInterfaces.webSockMessager)
        }
    }
}

