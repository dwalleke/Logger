package nl.orsit.menu.klanten;

import org.json.JSONException;
import org.json.JSONObject;

public class KlantItem {
    final private String key;

    private String naam;
    private String tel;
    private String email;
    private String postcode;
    private String huisnr;
    private String plaats;
    private String adres;

    public KlantItem() {
        this.key = null;
        this.naam = "Geen resultaat";
        this.plaats = "";
    }

    public KlantItem(String key, JSONObject value) {
        this.key = key;
        try {
            this.naam = value.getString("naam");
            this.tel = value.getString("tel");
            this.email = value.getString("email");
            this.postcode = value.getString("postcode");
            this.huisnr = value.getString("huisnr");
            this.plaats = value.getString("plaats");
            this.adres = value.getString("adres");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getKey() { return key; }

    public String getNaam() {
        return naam;
    }

    public String getTel() {
        return tel;
    }

    public String getEmail() {
        return email;
    }

    public String getPostcode() {
        return postcode;
    }

    public String getHuisnr() {
        return huisnr;
    }

    public String getPlaats() {
        return plaats;
    }

    public String getAdres() {
        return adres;
    }

}
