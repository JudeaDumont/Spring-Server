package server.springcontext.systemstate

import server.springcontext.utilities.JsonUtilities

object SystemStateStore {
    var fullState = HashMap<String, HashMap<String, String>>()

    fun updateFullState(category: String, message: String) {
        println("updateFullState"+message + " " + category)
        val hashMap = JsonUtilities.jsonStringToMap(message)
        println(category)

        //following line puts errors in their respective messages
        ServerState.serverStateDelta(category)?.let { hashMap.putAll(it) }
//        println(hashMap)
        if (!fullState.containsKey(category)) {
            fullState.put(category, HashMap(hashMap))
            SystemDeltas.deltasList.put(category, HashMap(hashMap))
        } else {
            if (!fullState.containsKey(category)) {
                fullState.put(category, HashMap())
            }
            SystemDeltas.extractDeltas(category, hashMap)
        }
    }
}