package server.springcontext.utilities

import com.google.gson.JsonArray
import com.google.gson.reflect.TypeToken
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject


object JsonUtilities {
    internal fun hashMapFromJsonArray(jsonArray: JsonArray): HashMap<String, String> {
        val returnValue = HashMap<String, String>()
        for (i in 0 until jsonArray.size()) {
            returnValue[jsonArray.get(i).asString] = ""
        }
        return returnValue
    }

    internal fun jsonStringToMap(jsonString:String) :HashMap<String,String> {
        return Gson().fromJson<HashMap<String, String>>(
                jsonString, object : TypeToken<HashMap<String, String>>() {
        }.type
        )
    }

    internal fun jsonStringToMapOfMaps(jsonString:String) :HashMap<String,HashMap<String, HashMap<String, String>>> {
        return Gson().fromJson<HashMap<String, HashMap<String, HashMap<String, String>>>>(
                jsonString, object : TypeToken<HashMap<String, HashMap<String, HashMap<String, String>>>>() {
        }.type
        )
    }

    fun jsonElementToArrayList(jsonElements: JsonElement): ArrayList<String> {
        val asJsonArray = jsonElements.asJsonArray
        val arrayList = ArrayList<String>()
        asJsonArray.mapTo(arrayList) { it.asString }
        return arrayList
    }

    fun printEntrySet(moduleSets: JsonObject) {
        for (module in moduleSets.entrySet()) {
            println(module.key)
        }
    }

}
