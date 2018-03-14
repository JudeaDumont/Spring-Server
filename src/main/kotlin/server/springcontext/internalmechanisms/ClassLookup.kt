package server.springcontext.internalmechanisms

object ClassLookup{
    var lookupTable = HashMap<String,String>()
    init{
        lookupTable.put("APP","server.springcontext.testmodules.APPModule")
    }
}