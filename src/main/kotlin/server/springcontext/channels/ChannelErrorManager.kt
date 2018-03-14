package server.springcontext.channels
//Knows if the channel mechanism is in an error state and has a list of methods that check that state
object ChannelErrorManager{
    var appNameToErrors = HashMap<String, HashMap<String,MessageStoppingErrorMethod>>()
    fun messageStoppingErrorsExist(channel:String): Boolean {


        //todo:only after configuring the channel definitions per app request can those channels actually be subbed to.


        //todo:another consideration is that if there are message stopping errors, all channels are unsubbed from


        //TODO: there should not be a channel passed in to this function.
        //todo: some configuration for expressing what apps are related to what channels is necessary for version checks.
        //todo:as in, the configuration that came in does not match what we have our channel names mapped to


        //if the app module isn't loaded it is guaranteed that the channels for that app arent being listened to
        //because an app is loaded when the device is discovered, this function will normally not have to do these checks.
        //todo: Device discovery changes the checks for message stopping errors
        //one check that will need to happen is that once the app is loaded it checks to see if it has a data definition afterwards
        //if it does not, errors are thrown in the sense that they are put into the message stopping errors container

        //todo: channel names need to be mapped to their associated app

//        InterestManager.moduleFullNameToModuleInstance.get(channel)
        val appName = channel.split(".")[0]
        println("ErrorManagerAppName"+appName)
        for (mutableEntry in
        appNameToErrors.get(appName)!!) {
            if (mutableEntry.value.run()) {
                println(mutableEntry.key)
                return true
            }
        }

        return false
    }
}