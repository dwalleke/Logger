package nl.orsit.menu.util;

import android.app.Activity;
import android.content.SharedPreferences;

import nl.orsit.base.PhpParams;
import nl.orsit.menu.MenuActivity;
import nl.orsit.menu.MenuDataInterface;
import nl.orsit.menu.data.MenuDataFragment;

public class MenuInfoReloader {

    private MenuDataFragment parent;
    private static MenuActivity activity;
    private static MenuInfoReloader instance;
    private static MenuDataInterface.LEVEL level;

    private MenuInfoReloader(MenuDataFragment parent, MenuActivity activity, MenuDataInterface.LEVEL level) {
        this.parent = parent;
        this.activity = activity;
        this.level = level;
    }

    public static void createInstance(MenuDataFragment parent, MenuActivity activity, MenuDataInterface.LEVEL level) {
        instance = new MenuInfoReloader(parent, activity, level);
    }

    public static void reload() {
        instance.reloadParent();
    }

    public static String getPref(String key) {
        SharedPreferences prefs = activity.getSharedPreferences("UserData", activity.MODE_PRIVATE);
        return prefs.getString(key, null);
    }

    private void reloadParent() {
        this.parent.loadDataset();
    }

    public static void setUserData(String bid, String mid, String kid, String obj) {
        SharedPreferences.Editor editor = activity.getSharedPreferences("UserData", activity.MODE_PRIVATE).edit();
        if (bid != null) {
            System.out.println("Setting new bid: " + bid);
            editor.putString("bid", bid);
        }
        if (mid != null) {
            System.out.println("Setting new mid: " + mid);
            editor.putString("mid", mid);
        }
        if (kid != null) {
            System.out.println("Setting new kid: " + kid);
            editor.putString("kid", kid);
        }
        if (obj != null) {
            System.out.println("Setting new obj: " + obj);
            editor.putString("obj", obj);
        }
        editor.apply();
        reload();
    }

    public static void savePref(String key, String value) {
        SharedPreferences.Editor editor = activity.getSharedPreferences("UserData", activity.MODE_PRIVATE).edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static MenuDataInterface.LEVEL getLevel() {
        return level;
    }

    public static MenuActivity getActivity() { return activity; }

}
