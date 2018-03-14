package server.springcontext.systemstate

import com.google.gson.JsonElement
import server.springcontext.utilities.JsonUtilities

object ServerState {
    /////////////////////////////////////////////category/////////code//value
    private val categoryToCodeAndValue = HashMap<String, HashMap<String, String>>()

    ///////////////////////////////////////code///////////category//NA
    private val codeToCategories = HashMap<String, ArrayList<String>>()

    fun addServerState(key: String, value: String, categoryRelevance: ArrayList<String>) {
        this.codeToCategories.putIfAbsent(key, categoryRelevance)
        for (category in categoryRelevance) {
            println("category" + category)
            this.categoryToCodeAndValue.putIfAbsent(category, HashMap())
            this.categoryToCodeAndValue[category]!!.put(key, value)
        }
    }

    fun removeServerState(key: String) {
        if (this.codeToCategories.contains(key)) {
            val cats = this.codeToCategories.get(key)!!
            for (i in 0 until cats.size) {
                if (categoryToCodeAndValue.contains(cats[i])) {
                    categoryToCodeAndValue.get(cats[i])!!.put(key, "")
                }
            }
        }
    }

    fun serverStateDelta(category: String): HashMap<String, String>? {
        //returns a hashmap full of serverside properties that have a relevance to this category
        //That hash map is merged with the new object that is going into the delta processor.
        return categoryToCodeAndValue[category]
    }
}
