package server.springcontext.testmodules

class TestConverter : Converter {
    override fun getProps(): HashMap<String, HashMap<String, RawPropExtracter>> {
        return abToRawAndAct
    }

    var abToRawAndAct = HashMap<String, HashMap<String, RawPropExtracter>>()

    val timeAct: RawPropExtracter = object : RawPropExtracter {
        override fun extract(args: ArrayList<String>): String {
            var TIME2 = args.get(0)
            var SYSTIME = args.get(1)
            return TIME2 + SYSTIME
        }
    }

    init {

        var time = HashMap<String, RawPropExtracter>()
        time.put("TIME2", timeAct)
        time.put("SYSTIME", timeAct)
        abToRawAndAct.put("time", time)
    }
}