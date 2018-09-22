package nl.orsit.base;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class PhpResult {

    private Map<String, String> errors = new HashMap<>();
    private Map<String, String> results = new HashMap<>();

    public PhpResult() {
        Map<String, String> errors = new HashMap<>();
        Map<String, String> results = new HashMap<>();
    }

    public PhpResult(String arg) {
        this();
        System.out.println("result#" + arg + "#");
        try {
            JSONObject out = new JSONObject(arg);
            fillMap(errors, out.getJSONObject("errors"));
            fillMap(results, out.getJSONObject("results"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void fillMap(Map<String, String> map, JSONObject input) throws JSONException {
        Iterator<String> it = input.keys();
        while (it.hasNext()) {
            String key = it.next();
            String value = input.getString(key);
            map.put(key, value);
        }
    }

    public PhpResult setError(String key, String error) {
        this.errors.put(key, error);
        return this;
    }

    public boolean isOk() {
        return this.errors.size() == 0;
    }

    public Map<String, String> getErrors() {
        return this.errors;
    }

    public Map<String, String> getResults() {
        return this.results;
    }
}
