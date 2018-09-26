package nl.orsit.menu.data;

import org.json.JSONException;
import org.json.JSONObject;


public class MasterType {
    private String id = "";
    private String omschrijving = "";

    public MasterType(JSONObject arg) {
        try {
            this.id = arg.getString("id");
            this.omschrijving = arg.getString("omschr");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getId() {
        return id;
    }

    public String getOmschrijving() {
        return omschrijving;
    }

}
