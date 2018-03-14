package server.springcontext.testmodules

interface Converter {
fun getProps():HashMap<String, HashMap<String, RawPropExtracter>>

}