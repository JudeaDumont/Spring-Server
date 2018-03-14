package server.springcontext.datadefinition;

import java.util.HashMap;

public class DataSchema {
    public HashMap<String,String> status;
    public HashMap<String,String> update;

    @Override
    public String toString() {
        if (status != null && update!=null) {
            return status.toString() + update.toString();
        } else {
            return "NULL";
        }
    }
}
