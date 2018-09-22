package nl.orsit.menu;

public class ListItem {
    final private String key;
    final private String value;
    public ListItem(String key, String value) {
        this.key = key;
        this.value = value;
    }
    public String getKey() { return key; }
    public String getValue() { return value; }
}
