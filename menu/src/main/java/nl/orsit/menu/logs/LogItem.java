package nl.orsit.menu.logs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import nl.orsit.menu.data.LogTypes;

public class LogItem {
    private String key;

    private Date datum;
    private String status;
    private String type;
    private boolean checked;
    private Map<String, LogItemType> logs;
    private LogTypes logTypes;

    public LogItem(LogTypes logTypes) {
        this.logTypes = logTypes;
        this.logs = new HashMap<>();
        this.datum = new Date();
        this.key = null;
        this.status = null;
        this.type = null;
        this.checked = false;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LogItem(JSONObject value, LogTypes logTypes) {
        this(logTypes);
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String datumString = null;
        this.logs = new HashMap<>();
        try {
            this.key = value.getString("lid");
            datumString = value.getString("datum");
            this.datum = format.parse(datumString);
            this.status = value.getString("status");
            this.type = value.getString("typex");
            this.checked = value.getString("checked").equals("1");
            System.out.println("logs: " + value.get("logs"));
            JSONArray loglist = new JSONArray(value.getString("logs"));
            for (int i = 0; i < loglist.length(); i++) {
                JSONObject log = (JSONObject) loglist.get(i);
                String lid = log.getString("lid");
                String invoer = log.getString("invoer");
                String val = log.getString("value");
                this.logs.put(invoer, new LogItemType(lid, invoer, val, this.logTypes));
            }
//            JSONObject loglist = new JSONObject(value.getString("logs"));
//            Iterator<String> it = loglist.keys();
//            while (it.hasNext()) {
//                String j = it.next();
//                String val = loglist.getString(j);
//                this.logs.put(j, new LogItemType(j, val, this.logTypes));
//            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public LogTypes getLogTypes() {
        return this.logTypes;
    }

    public String getKey() { return key; }

    public Date getDatum() {
        return datum;
    }

    public String getStatus() {
        return status;
    }

    public String getType() { return type; }

    public boolean isChecked() {
        return checked;
    }

    public Map<String, LogItemType> getLogs() {
        return logs;
    }
}
