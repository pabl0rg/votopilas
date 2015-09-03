package com.crealoya.votopilas.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by @pablopantaleon on 7/20/15.
 */
public class PreferencesUtil {

    public final static String PREF_FIRST_RUN = "first_run";
    public final static String PREF_LOGIN_DIALOG_SHOWED = "login_dialog_showed";
    public final static String PREF_HIDEN_PARTIES = "hidden.parties";
    public final static String PREF_RATINGS_TO_APPLY = "ratings_to_apply";

    private final static String PREFERENCES = "votopilas";
    private static SharedPreferences mPreferences;

    public static void init(Context context) {
        mPreferences = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
    }

    public static boolean getBooleanPreference(String key, boolean defaultValue) {
        return mPreferences.getBoolean(key, defaultValue);
    }

    public static void setBooleanPreference(String key, boolean value) {
        mPreferences.edit().putBoolean(key, value).commit();
    }

    public static Set<String> getSetPreference(String key, Set<String> defaultValue) {
        return mPreferences.getStringSet(key, defaultValue);
    }

    public static void setSetPreference(String key, Set<String> value) {
        mPreferences.edit().remove(key).commit();
        mPreferences.edit().putStringSet(key, value).commit();
    }

    public static void clearAllPreferencesThatStartWith(String key) {
        Map<String, ?> map = mPreferences.getAll();

        Set<String> keys = map.keySet();
        SharedPreferences.Editor editor = mPreferences.edit();
        for (String k : keys) {
            if (k.startsWith(key)) {
                editor.remove(k);
            }
        }
        editor.commit();
        mPreferences.edit().remove(key).commit();
    }
}
