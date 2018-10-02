package nl.orsit.menu.data;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

public class MasterStatus {

    private String id = "";
    private String masterType = "";
    private String omschrijving = "";

    public MasterStatus(String omschrijving) {
        this.omschrijving = omschrijving;
    }

    public MasterStatus(JSONObject arg) {

        try {
            this.id = arg.getString("id");
            this.masterType = arg.getString("master_type");
            this.omschrijving = arg.getString("omschr");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getId() {
        return id;
    }

    public String getMasterType() {
        return masterType;
    }

    public String getOmschrijving() {
        return omschrijving;
    }


    //to display object as a string in spinner
    @Override
    public String toString() {
        return omschrijving;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof MasterStatus){
            MasterStatus c = (MasterStatus)obj;
            if(c.getId()==id ) return true;
        }
        return false;
    }


}
