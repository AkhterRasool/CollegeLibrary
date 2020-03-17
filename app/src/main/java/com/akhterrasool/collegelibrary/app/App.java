package com.akhterrasool.collegelibrary.app;

import android.content.Context;
import android.content.SharedPreferences;

import com.akhterrasool.collegelibrary.R;

public class App {

    private static Context appContext;

    public static Context getContext() {
        return appContext;
    }

    public static void setContext(Context context) {
        if (appContext == null) {
            appContext = context;
        }
    }

    public static SharedPreferences getSearchHistoryPreference() {
        return appContext.getSharedPreferences(
                appContext.getString(R.string.search_history_file), Context.MODE_PRIVATE);
    }

    public static boolean searchHistoryEntriesExist() {
        return getSearchHistoryPreference().getAll().size() > 0;
    }
}
