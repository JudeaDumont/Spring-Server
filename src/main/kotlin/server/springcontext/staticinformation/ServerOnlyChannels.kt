package server.springcontext.staticinformation

object ServerOnlyChannels{
    val serverOnlyChannels = HashMap<String,String>()
    init{
        serverOnlyChannels.put("APP.requestDataSchema","")
    }
}