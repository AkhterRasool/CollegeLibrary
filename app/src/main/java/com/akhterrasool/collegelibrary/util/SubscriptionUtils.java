package com.akhterrasool.collegelibrary.util;

import android.content.SharedPreferences;
import android.util.Log;

import com.akhterrasool.collegelibrary.app.App;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class SubscriptionUtils {

    private static final String TAG = "SubscriptionUtils";
    private static final String NOTIFICATION_ITEMS_KEY = "NotificationItems";
    private static Set<String> subscriptions;
    private static final SubscriptionUtils lock;

    static {
        lock = new SubscriptionUtils();
        subscriptions = getSubscriptionItems();
    }

    public static void addItemToSubscription(String input) {
        if (!subscriptions.contains(input)) {
            synchronized (lock) {
                subscriptions.add(input);
            }
            updateSubscriptions();
        }
    }

    private static void updateSubscriptions() {
        SharedPreferences.Editor editor = App.getNotificationPreference().edit();
        editor.putStringSet(NOTIFICATION_ITEMS_KEY, subscriptions);
        editor.apply();
    }

    public static Set<String> getSubscriptionItems() {
        if (subscriptions == null) {
            subscriptions = new HashSet<>(App.getNotificationPreference().getStringSet(NOTIFICATION_ITEMS_KEY, Collections.emptySet()));
        }
        return subscriptions;
    }

    public static void clearAllNotificationItems() {
        synchronized (lock) {
            if (atLeastOneNotificationItemExists()) {
                subscriptions = new HashSet<>();
                SharedPreferences.Editor editor = App.getNotificationPreference().edit();
                editor.clear();
                editor.apply();
            }
        }
    }

    public static boolean atLeastOneNotificationItemExists() {
        return subscriptions.size() > 0;
    }

    public static void removeItemFromSubscription(String item) {
        Log.i(TAG, "removeItemFromSubscription: Removing '" + item + "'");
        synchronized (lock) {
            subscriptions.remove(item);
        }
        updateSubscriptions();
    }
}
