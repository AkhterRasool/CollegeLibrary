package com.akhterrasool.collegelibrary.activity.notification;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.akhterrasool.collegelibrary.R;
import com.akhterrasool.collegelibrary.activity.HomeActivity;
import com.akhterrasool.collegelibrary.util.ActivityUtils;
import com.akhterrasool.collegelibrary.util.SubscriptionUtils;

import java.util.Set;

import static com.akhterrasool.collegelibrary.app.Constants.NEW_LINE;
import static com.akhterrasool.collegelibrary.util.AppUtils.getResourceString;
import static com.akhterrasool.collegelibrary.util.AppUtils.showLong;
import static com.akhterrasool.collegelibrary.util.SubscriptionUtils.atLeastOneNotificationItemExists;
import static com.akhterrasool.collegelibrary.util.SubscriptionUtils.clearAllNotificationItems;

/**
 * Displays the Activity when the user clicks NOTIFICATIONS button in {@link HomeActivity}
 */
public class MainNotificationActivity extends AppCompatActivity {

    //TAG used for logging purpose i.e, to identify from which class the log message is coming from.
    private static final String TAG = "com.akhterrasool.collegelibrary.activity.notification.MainNotificationActivity";
    private Button registerBooksForNotificationsButton;
    private Button viewRegisteredBooksButton;
    private Button stopNotificationsButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification_home_layout);

        registerBooksForNotificationsButton = findViewById(R.id.register_books_notifications_button);
        stopNotificationsButton = findViewById(R.id.notification_stop_button);
        viewRegisteredBooksButton = findViewById(R.id.view_registered_books_for_notifications_button);


        registerBooksForNotificationsButton.setOnClickListener(view -> ActivityUtils.startActivity(NotificationRegistrationActivity.class));

        stopNotificationsButton.setOnClickListener(view -> clearAllNotificationItemsFromSubscription());

        viewRegisteredBooksButton.setOnClickListener(view -> displayBooksRegistered());

    }

    private void displayBooksRegistered() {
        Log.i(TAG, "displayBooksRegistered: Checking if at least one item has been registered to list.");
        if (atLeastOneNotificationItemExists()) {
            Log.i(TAG, "displayBooksRegistered: One or more items have been registered.");
            //Use StringBuffer instead of String for faster performance.
            StringBuffer result = new StringBuffer();
            Set<String> subscriptionSet = SubscriptionUtils.getSubscriptionItems();
            /*
                It should look some thing like this in ResultActivity:

             *  These are the following items you have registered:
             *  1) Embedded Systems
             *  2) Operating Systems
             */
            int counter = 1;
            for (String item : subscriptionSet) {
                result.append(String.format("%d) %s%s%s", counter++, item, NEW_LINE, NEW_LINE));
                //String formatted would be something like
                //1) Embedded Systems\n\n
            }

            Log.i(TAG, "displayBooksRegistered: Going to display registered items.");
            String title = getResourceString(R.string.notification_items_list_display_title);
            ActivityUtils.startResultActivity(title, result.toString());
        } else {
            Log.i(TAG, "displayBooksRegistered: No items have been registered.");
            showLong(getResourceString(R.string.nothing_to_display));
        }
    }

    private void clearAllNotificationItemsFromSubscription() {
        Log.i(TAG, "displayBooksRegistered: Checking if at least one item has been registered to list.");
        if (atLeastOneNotificationItemExists()) {
            Log.i(TAG, "clearAllNotificationItemsFromSubscription: Clearing items");
            clearAllNotificationItems();
            Log.i(TAG, "clearAllNotificationItemsFromSubscription: Items have been cleared.");
            showLong(getResourceString(R.string.all_items_are_cleared));
        } else {
            Log.i(TAG, "clearAllNotificationItemsFromSubscription: Nothing to clear.");
            showLong(getResourceString(R.string.nothing_to_clear));
        }
    }
}
