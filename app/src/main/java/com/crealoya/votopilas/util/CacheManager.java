package com.crealoya.votopilas.util;

import android.support.annotation.NonNull;

import com.crealoya.votopilas.network.Callback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;

import java.util.List;

/**
 * Created by @pablopantaleon on 7/29/15.
 */
public class CacheManager {

    public static final String PREF_LIST_NAME_CACHE = "pref.list_name_cache";
    public static final String PREF_PARTY_CACHE = "pref.party_cache";
    public static final String PREF_CHARGE_CACHE = "pref.charge_cache";
    public static final String PREF_LIST_POSITION = "pref.list_position";

    public static boolean isQueryCache(@NonNull String prefKey) {
        return PreferencesUtil.getBooleanPreference(prefKey, false);
    }

    /**
     * Save in local data store all retrieved objects from Parse (only save ParseObjects)
     * @param tag query tag. Let us retrieve objects for specific tab
     * @param prefKey name for cache variable. @see PreferencesUtil.class
     * @param data that will be stored in local datastore
     */
    public static <T extends ParseObject> void setListQueryCache(String tag, final String prefKey,
                                                             final List<T> data, final Callback callback) {
        ParseObject.pinAllInBackground(tag, data, new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    if (callback != null) {
                        callback.done(false, null);
                    }
                } else {
                    PreferencesUtil.setBooleanPreference(prefKey, true);

                    if (callback != null) {
                        callback.done(true, data);
                    }
                }
            }
        });
    }

    public static void removeQueryCache() {
        // TODO: unpin and pin again when refresh is ready
    }
}
