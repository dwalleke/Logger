package nl.orsit.menu.logs;

import org.json.JSONObject;

import nl.orsit.menu.MenuDataInterface;

public class LogItemType {


    private MenuDataInterface.LOG_TYPE type;
    private String id;
    private String val;

    public LogItemType(String j, String val, MenuDataInterface parent) {
        this.id = j;
        this.val = val;
        this.type = parent.getLogTypes().getLogType(j);
    }


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
