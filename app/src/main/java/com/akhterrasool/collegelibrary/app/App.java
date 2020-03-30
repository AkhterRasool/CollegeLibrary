package com.akhterrasool.collegelibrary.app;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import com.akhterrasool.collegelibrary.R;

import static com.akhterrasool.collegelibrary.notification.SearchNotification.CHANNEL_ID;
import static com.akhterrasool.collegelibrary.util.AppUtils.getResourceString;

public class App {

    private static Context appContext;

    public static Context getContext() {
        return appContext;
    }

    public static void init(Context context) {
        if (appContext == null) {
            appContext = context;
        }

        createSearchNotificationChannel();
    }

    public static SharedPreferences getSearchHistoryPreference() {
        return appContext.getSharedPreferences(
                appContext.getString(R.string.search_history_file), Context.MODE_PRIVATE);
    }

    public static SharedPreferences getNotificationPreference() {
        return appContext.getSharedPreferences(
                appContext.getString(R.string.notification_preference), Context.MODE_PRIVATE);
    }

    public static boolean searchHistoryEntriesExist() {
        return getSearchHistoryPreference().getAll().size() > 0;
    }

    private static void createSearchNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getResourceString(R.string.search_notification_channel_name);
            String description = getResourceString(R.string.search_notification_channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = App.getContext().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public static void setContext(Context context) {
        if (context != null) {
            appContext = context;
        }
    }
}
