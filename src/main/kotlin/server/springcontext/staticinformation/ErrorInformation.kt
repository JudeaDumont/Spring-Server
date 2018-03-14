package server.springcontext.staticinformation

import server.springcontext.utilities.JSON

object ErrorInformation {
    fun getDataDefinitionError(appName: String, info: String = ""): String {
        return JSON.stringify(ErrorObj("datadefinition", "datadefinition for '$appName' has not been set yet$info", ErrorData()))
    }

//    fun getDataDefinitionErrorCats(categories: ArrayList<String>): String {
//        return JSON.stringify(categories)
//    }

    fun getDataDefinitionUndefinedErrorKey(): String {
        return "dataDefinitionError"
    }

//    The categories an error has to deal with is app specific
    //todo:the categories an app has to deal with needs to be dynamically decided with regard to what the app name is and the prefix of the category

    fun getChannelDefinitionError(channel: String, info: String = ""): String {
        return JSON.stringify(ErrorObj("channeldefinition", "channeldefinition '$channel' has not been set yet$info", ErrorData()))
    }

    fun getChannelDefinitionUndefinedErrorKey(): String {
        return "dataDefinitionError"
    }

//    fun getChannelDefinitionErrorCats(categories: ArrayList<String>): String {
//        return JSON.stringify(categories)
//    }

    fun getWebSocketCannotBeCreatedError(info: String) {
        "ERROR INSTANTIATING WEBSOCKET MESSAGING TEMPLATE:" + info
    }

    fun getModuleUnavailableError(moduleName: String, info: String = ""): String {
        return JSON.stringify(ErrorObj("moduleUnavailable", "module '$moduleName' is unavailable$info", ErrorData()))
    }

    fun getModuleUnavailableErrorKey(): String {
        return "moduleUnavailableError"
    }

//    fun getModuleUnavailableErrorCats(categories: ArrayList<String>): String {
//        return JSON.stringify(categories)
//    }

    //todo: the channel names will have to be dynamic as well.
    //
}