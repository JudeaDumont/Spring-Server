package server.springcontext.redis

import java.util.concurrent.CountDownLatch

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.messaging.simp.SimpMessagingTemplate
import server.springcontext.channels.ChannelErrorManager
import server.springcontext.channels.ServerChannelManager
import server.springcontext.outpoint.ClientMessager

class RedisInterfaces @Autowired
constructor(private val latch: CountDownLatch) {

    @Autowired
            //this is to arbitrarily broadcast messages through channels without acks
    var webSocketMessager: SimpMessagingTemplate? = null

    //todo:There should also be a different listener per application
    //todo:the data definition should be per application as well.
    //todo: add applications through service discovery
    //todo:upon discovering an application, the server asks it to broadcast its data definition.
    //receives all messages on all channels
    fun receiveMessage(message: String, channel: String): String {
//        println(channel)
        latch.countDown()
        //todo: server channels should have separate listener
        //todo: if catastrophic errors occur, the recieving message system should just be turned off
        //todo: the recieving message system should not keep recieving deltas and throwing them away due to an error check.
        if (!ServerChannelManager.serverOnlyChannel(channel)) {//check for channels that are only for the server
            if (!ChannelErrorManager.messageStoppingErrorsExist(channel)) {//check for errors that stop message propagation
                //Normal Messaging with Redis Content Included
//                println(channel + ": " + message)
                ClientMessager.beginMessagingFiltration(channel, message, webSocketMessager)
            } else { //ServerMessages Can Still get through
                ClientMessager.beginMessagingFiltration(channel, "{}", webSocketMessager)
            }

            if (webSocketMessager == null) {
                //Value potential for switching to HTTPS requests if this occurs
//                println(ErrorInformation.webSocketCannotBeCreatedError)
            }
        } else {//process channel that is only for server
            //todo:server should have its own listener and a different version fo redis interface should be used.
            ServerChannelManager.processServerOnlyChannel(channel, message)
        }
        return (message)
    }

    companion object {
        @JvmStatic
        var redisMessager: StringRedisTemplate? = null

        @JvmStatic
        var webSockMessager: SimpMessagingTemplate? = null
    }
    init{
        webSockMessager = webSocketMessager
    }
}