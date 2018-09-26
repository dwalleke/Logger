package nl.orsit.menu.data;

import org.json.JSONException;
import org.json.JSONObject;

public class MasterInvoer {
    private String id;
    private String omschr;
    private String invtype;

    public MasterInvoer(JSONObject arg) {
        try {
            this.id = arg.getString("id");
            this.omschr = arg.getString("omschr");
            this.invtype = arg.getString("invtype");
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public String getId() {
        return id;
    }

    public String getOmschrijving() {
        return omschr;
    }

    public String getInvoerType() {
        return invtype;
    }

}
