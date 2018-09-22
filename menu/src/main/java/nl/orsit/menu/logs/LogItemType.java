package nl.orsit.menu.logs;

import org.json.JSONObject;

public class LogItemType {

    public enum TYPE { MultiText, Number, Text, Image, Choice }
    private TYPE type;
    private Object value;
    private int key;


}
