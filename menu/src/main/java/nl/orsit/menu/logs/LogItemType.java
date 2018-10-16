package nl.orsit.menu.logs;

import org.json.JSONObject;

import nl.orsit.menu.MenuDataInterface;
import nl.orsit.menu.data.LogTypes;

public class LogItemType {


    private MenuDataInterface.LOG_TYPE type;
    private String lid;
    private String id;
    private String val;

    public LogItemType(String lid, String invoer, String value, LogTypes logTypes) {
        this.lid = lid;
        this.id = invoer;
        this.val = value;
        this.type = logTypes.getLogType(invoer);
    }

    public String getLid() { return lid; }

    public String getId() {
        return id;
    }

    public String getValue() {
        return val;
    }

    public MenuDataInterface.LOG_TYPE getType() {
        return type;
    }



}
