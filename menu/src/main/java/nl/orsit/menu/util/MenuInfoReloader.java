package nl.orsit.menu.util;

import android.app.Activity;
import android.content.SharedPreferences;

import nl.orsit.menu.MenuDataInterface;
import nl.orsit.menu.data.MenuDataFragment;

public class MenuInfoReloader {

    private MenuDataFragment parent;
    private static Activity activity;
    private static MenuInfoReloader instance;
    private static MenuDataInterface.LEVEL level;

    private MenuInfoReloader(MenuDataFragment parent, Activity activity, MenuDataInterface.LEVEL level) {
        this.parent = parent;
        this.activity = activity;
        this.level = level;
    }

    public static void createInstance(MenuDataFragment parent, Activity activity, MenuDataInterface.LEVEL level) {
        instance = new MenuInfoReloader(parent, activity, level);
    }

    public static void reload() {
        instance.reloadParent();
    }

    private void reloadParent() {
        this.parent.loadDataset();
    }

    public static void setUserData(String bid, String mid, String kid, String obj) {
        SharedPreferences.Editor editor = activity.getSharedPreferences("UserData", activity.MODE_PRIVATE).edit();
        if (bid != null) {
            editor.putString("bid", bid);
        }
        if (mid != null) {
            editor.putString("mid", mid);
        }
        if (kid != null) {
            editor.putString("kid", kid);
        }
        if (obj != null) {
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


}
