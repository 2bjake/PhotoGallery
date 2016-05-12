package com.bignerdranch.android.photogallery;

import android.content.Context;
import android.preference.PreferenceManager;

/**
 * Created by jakefost on 5/12/16.
 */
public class QueryPreferences {
    private static final String PREF_SEARCH_QUERY = "searchQuery";
    private static final String PREF_LAST_RESULT_ID = "lastResultId";

    public static String getStoredQuery(Context context) {
        return getValue(context, PREF_SEARCH_QUERY);
    }

    public static void setStoredQuery(Context context, String query) {
        setValue(context, PREF_SEARCH_QUERY, query);
    }

    public static String getLastResultId(Context context) {
        return getValue(context, PREF_LAST_RESULT_ID);
    }

    public static void setLastResultId(Context context, String lastResultId) {
        setValue(context, PREF_LAST_RESULT_ID, lastResultId);
    }

    private static String getValue(Context context, String key) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(key, null);
    }

    private static void setValue(Context context, String key, String value) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(key, value)
                .apply();
    }
}
