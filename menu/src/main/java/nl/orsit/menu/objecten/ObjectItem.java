package nl.orsit.menu.objecten;

import org.json.JSONException;
import org.json.JSONObject;

import nl.orsit.menu.data.LogTypes;

public class ObjectItem {
    private String key;
    private String soort;
    private String code;
    private String merk;
    private String type;
    private String serienr;
    private LogTypes logTypes;


    public ObjectItem(LogTypes logTypes, JSONObject value) {
        this.logTypes = logTypes;
        try {
            this.key = value.getString("qr");
            this.code = value.getString("code");
            this.soort = value.getString("soort");
            this.merk = value.getString("merk");
            this.type = value.getString("type");
            this.serienr = value.getString("serienr");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public ObjectItem() {
        this.key = null;
        this.merk = null;
        this.type = null;
    }

    public String getOmschrijving() {
        return logTypes.getUserType(this.soort).getOmschrijving();
    }

    public String getCode() { return key; }

    public String getKey() { return key; }

    public String getSoort() {
        return soort;
    }

    public String getMerk() {
        return merk;
    }

    public String getType() {
        return type;
    }

    public String getSerienr() {
        return serienr;
    }


}
