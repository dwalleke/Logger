package nl.orsit.base;

import java.util.HashMap;

public class PhpParams extends HashMap<String, String> {


    public PhpParams add(String key, String value) {
        this.put(key, value);
        return this;
    }
}
