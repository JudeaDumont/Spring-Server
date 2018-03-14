package server.springcontext.utilities

import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import java.lang.reflect.Type


object JSON {
        private val gson = Gson()
        private val gsonParser = JsonParser()
        fun stringify(src:Any):String{
            return gson.toJson(src)
        }
        fun parse(jsonLine: String): JsonObject {
            return gsonParser.parse(jsonLine).asJsonObject
        }
        fun parseArray(jsonLine: String): JsonArray {
            return gsonParser.parse(jsonLine).asJsonArray
        }
        fun parse(jsonLine: String, classForConversion: Type): Any {
            return (gson.fromJson(jsonLine,classForConversion))
        }
//        @JvmStatic
//        fun parse(jsonLine: String, classForConversion:Class<DataSchema>): ClientModelInterests {
//            return (gson.fromJson(jsonLine,classForConversion))
//        }
}

