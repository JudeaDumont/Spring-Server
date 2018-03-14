package server.springcontext.channels
//Used to provide function pointer semantics to various error state check methods
interface MessageStoppingErrorMethod {
    fun run() : Boolean
}