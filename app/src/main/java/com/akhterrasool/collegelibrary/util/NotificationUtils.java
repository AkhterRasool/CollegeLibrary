package com.akhterrasool.collegelibrary.util;

import android.content.SharedPreferences;
import android.util.Log;

import com.akhterrasool.collegelibrary.app.App;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class NotificationUtils {

    private static final String NOTIFICATION_ITEMS_KEY = "NotificationItems";
    private static final String TAG = "com.akhterrasool.collegelibrary.util.NotificationUtils";

    public static void addItemToSubscription(String input) {
        Set<String> existingSet = getSubscriptionItems();
        existingSet.add(input);
        updateSubscriptions(existingSet);
    }

    private static void updateSubscriptions(Set<String> updatedSet) {
        SharedPreferences.Editor editor = App.getNotificationPreference().edit();
        editor.putStringSet(NOTIFICATION_ITEMS_KEY, updatedSet);
        editor.commit();
    }

    public static Set<String> getSubscriptionItems() {
        return App.getNotificationPreference().getStringSet(NOTIFICATION_ITEMS_KEY, new HashSet<>());
    }

    public static void clearAllNotificationItems() {
        SharedPreferences.Editor editor = App.getNotificationPreference().edit();
        editor.clear();
        editor.commit();
    }

    public static boolean atLeastOneNotificationItemExists() {
        return getSubscriptionItems().size() > 0;
    }

    public static void removeItemFromSubscription(String item) {
        Log.i(TAG, "removeItemFromSubscription: Removing '" + item + "'");
        Set<String> updatedSet = getSubscriptionItems()
                .stream()
                .filter(existingItem -> !existingItem.equals(item))
                .collect(Collectors.toSet());
        updateSubscriptions(updatedSet);
    }
}
