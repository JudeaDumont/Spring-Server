package server.springcontext.interestsmechanism

class InterestsChangeRequestObject {
    //This is used when a message is sent from the client through the
    // websocket connection to the server and has to do with interests
    var name: String? = "DEFAULT"
    var channelName: String? = "DEFAULT"

    //This is required for serialization/deserialization, do not remove
    constructor(){}

    constructor(name: String, channelName:String, app:String) {
        this.name = name
        this.channelName = channelName
    }
}