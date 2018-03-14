package server.springcontext.interestsmechanism

import java.util.HashMap

object SessionManager{
    var sessionIdToClientChannel = HashMap<String, String>()

    fun mapSessionToChannel(sessionId: String, channelName: String) {
        sessionIdToClientChannel[sessionId] = channelName
    }
}