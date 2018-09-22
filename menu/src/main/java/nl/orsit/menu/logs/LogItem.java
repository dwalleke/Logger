package nl.orsit.menu.logs;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.Map;

public class LogItem {
    final private String key;

    private Date datum;
    private String status;
    private boolean checked;
    private Map<Integer, LogItemType> logs;


    public LogItem(String key, JSONObject value) {
        this.key = key;
        try {
            this.status = value.getString("status");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getKey() { return key; }

}
