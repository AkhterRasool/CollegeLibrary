package com.akhterrasool.collegelibrary.util;

import android.content.SharedPreferences;
import android.util.Log;

import com.akhterrasool.collegelibrary.app.App;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * This class is used to provide some utility methods to make modifications and to
 * retrieve the subscription list.
 *
 * From the NOTIFICATIONS preference file, we store the subscription list as follows
 * <p>
 *     NotificationItems ===> ["Embedded Systems"] (user registers an item)
 *     NotificationItems ===> ["Embedded Systems", "Operating Systems"] (user registers another item)
 *     NotificationItems ===> [] (user clears the subscription set)
 * </p>
 *
 * Stored as key-value pair
 * where the key is NotificationItems
 * and the value is the subscription set.
 *
 * For modifications, no new key is created (if already existed). The same key is used to refer the set.
 * Only the set is updated and put into the same key.
 *
 * The NOTIFICATIONS shared preference file is only used for keeping track of subscription set
 * and nothing else. Please create another shared preference file if your objectives are different.
 */
public class SubscriptionUtils {

    //The subscription set's key.
    private static final String NOTIFICATION_ITEMS_KEY = "NotificationItems";
    //TAG used for logging purpose i.e, to identify from which class the log message is coming from.
    private static final String TAG = "com.akhterrasool.collegelibrary.util.NotificationUtils";

    public static void addItemToSubscription(String input) {
        //Get the subscription items and put those items in a new Set container.
        //This is done so as to avoid modifying the set container of the preference file as
        //it's not reliable to modify the direct set returned from the NOTIFICATIONS preference file.
        //The changes may or may not be applied if modifications are done to the direct set returned from getSubscriptionItems().
        //The values are copied to a new container and then the new container is modified and sent for update.
        Set<String> existingSet = new HashSet<>(getSubscriptionItems());
        existingSet.add(input);
        updateSubscriptions(existingSet);
    }

    private static void updateSubscriptions(Set<String> updatedSet) {
        SharedPreferences.Editor editor = App.getNotificationPreference().edit();
        editor.putStringSet(NOTIFICATION_ITEMS_KEY, updatedSet); //For the same key just put the updated set.
        //After making modifications we need to commit the set to the file.
        editor.commit();//This is mandatory otherwise changes will not be saved/reflected.
    }

    public static Set<String> getSubscriptionItems() {
        //If there's no such key in NOTIFICATIONS file an empty Set is retrieved.
        return App.getNotificationPreference()
                .getStringSet(
                        NOTIFICATION_ITEMS_KEY, //Get the existing set for this key if this key exists
                        new HashSet<>()); //Otherwise return an empty set as an alternative.
    }

    public static void clearAllNotificationItems() {
        //We invoke the editor to make changes into the preference file if any.
        SharedPreferences.Editor editor = App.getNotificationPreference().edit();
        //Clear everything in the NOTIFICATIONS file and commit.
        editor.clear();
        editor.commit(); //Commit ensures that changes are saved/reflected.
    }

    public static boolean atLeastOneNotificationItemExists() {
        return getSubscriptionItems().size() > 0;
    }

    public static void removeItemFromSubscription(String item) {
        Log.i(TAG, "removeItemFromSubscription: Removing '" + item + "'");
        //Get the subscription items and put those items in a new Set container.
        //This is done so as to avoid modifying the set container of the preference file as
        //it's not reliable to modify the direct set returned from the NOTIFICATIONS preference file.
        //The values are copied to a new container and then the new container is modified and sent for update.
        Set<String> updatedSet = new HashSet<>(getSubscriptionItems())
                .stream() //Convert it into stream for filtering.
                .filter(existingItem -> !existingItem.equals(item))//From existing filter(i.e consider) those items which are not equal to item.
                .collect(Collectors.toSet());//After filtering collect the filtered items into a set and return the set of those collected items.
        updateSubscriptions(updatedSet);
    }
}


