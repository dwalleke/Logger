package nl.orsit.base;

import android.support.annotation.RestrictTo;

public class ScrollingTableItem {

    private String label;
    private String id;
    private String value;

    public ScrollingTableItem(String label, String id, String value) {
        this.label = label;
        this.id = id;
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public String getId() {
        return id;
    }

    public String getValue() {
        return value;
    }
}
