package server.springcontext.utilities

object DataStructureConverter {

    fun arrayToHashMap(arrayToConvert: ArrayList<String>): HashMap<String, String> {
        var hashMap = HashMap<String, String>()
        for (item in arrayToConvert) {
            hashMap.put(item, "")
        }
        return hashMap
    }
}