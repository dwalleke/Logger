package nl.orsit.menu.logs;

import org.json.JSONException;
import org.json.JSONObject;

public class UserConfig {

    private String user_type;
    private String user_status;
    private String user_invoer;


    public UserConfig(JSONObject arg) {
        try {
            this.user_type = arg.getString("user_type");
            this.user_status = arg.getString("user_status");
            this.user_invoer = arg.getString("user_invoer");
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    public String getUser_type() {
        return user_type;
    }

    public String getUser_status() {
        return user_status;
    }

    public String getUser_invoer() {
        return user_invoer;
    }
}
