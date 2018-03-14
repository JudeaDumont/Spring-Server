package server.springcontext.interestsmechanism.clientmodelinterests;

import server.springcontext.utilities.JsonAble;

import java.util.HashMap;

public class ClientModelInterests implements JsonAble {
    public HashMap<String,AppInterest> apps;
    public String channelName;
}
