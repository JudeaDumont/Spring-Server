package server.springcontext.systemstate

object SystemDeltas {
    var deltasList = HashMap<String, HashMap<String, String>>()
    fun extractDeltas(category: String, hashMap: HashMap<String, String>) {
//        println("HASH"+hashMap)
        for (prop in hashMap) {
            if (!prop.value.equals(SystemStateStore.fullState.get(category)!!.get(prop.key))) {
                deltasList.get(category)!!.put(prop.key, hashMap.get(prop.key)!!)
                SystemStateStore.fullState.get(category)!!.put(prop.key, hashMap.get(prop.key)!!)
            } else {
                deltasList.get(category)!!.remove(prop.key)
            }
        }
    }
}
