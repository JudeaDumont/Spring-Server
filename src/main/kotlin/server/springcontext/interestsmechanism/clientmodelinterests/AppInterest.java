package server.springcontext.interestsmechanism.clientmodelinterests;

import java.util.ArrayList;

public class AppInterest {
    public ArrayList<String> options;
    public ArrayList<String> cats;
    public ArrayList<String> props;

    @Override
    public String toString() {
        return options.toString() +
                cats.toString() +
                props.toString();
    }
}
