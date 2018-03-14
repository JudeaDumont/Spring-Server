package server.springcontext.channels

//provides function pointer semantics for methods to call upon recieving a publish on a server owned channel
interface ServerOnlyChannelMethodI {
    fun run(jsonString:String)
}