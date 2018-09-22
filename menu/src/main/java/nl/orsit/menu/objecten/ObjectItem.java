package nl.orsit.menu.objecten;

import org.json.JSONException;
import org.json.JSONObject;

public class ObjectItem {
    final private String key;

    private String soort;
    private String merk;
    private String type;
    private String serienr;


    public ObjectItem(String key, JSONObject value) {
        this.key = key;
        try {
            this.soort = value.getString("omschr");
            this.merk = value.getString("merk");
            this.type = value.getString("type");
            this.serienr = value.getString("serienr");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

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
