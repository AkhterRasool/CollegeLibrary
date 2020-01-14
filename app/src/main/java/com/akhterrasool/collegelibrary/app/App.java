package com.akhterrasool.collegelibrary.app;

import android.content.Context;

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
}
