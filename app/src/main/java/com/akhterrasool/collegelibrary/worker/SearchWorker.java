package com.akhterrasool.collegelibrary.worker;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.akhterrasool.collegelibrary.R;
import com.akhterrasool.collegelibrary.activity.notification.MainNotificationActivity;
import com.akhterrasool.collegelibrary.app.App;
import com.akhterrasool.collegelibrary.clientrequest.NotifiableTitleResults;
import com.akhterrasool.collegelibrary.notification.SearchNotification;
import com.akhterrasool.collegelibrary.service.SearchService;
import com.akhterrasool.collegelibrary.util.Client;
import com.akhterrasool.collegelibrary.util.SubscriptionUtils;

import java.time.Duration;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static androidx.work.ListenableWorker.Result.failure;
import static androidx.work.ListenableWorker.Result.success;
import static com.akhterrasool.collegelibrary.util.AppUtils.getResourceString;

/**
 * A Worker is part of Android WorkManager API which focuses on executing work
 * in background. This type of work guarantees complete work execution.
 *
 * This type of background action (from WorkManager API) is from modern Android development.
 * Hence it's preferable to use such Worker instances to perform background work as it is
 * backwards compatible, i.e, works well with older devices.
 *
 * For our scenario, {@link SearchWorker} is a type of Worker which searches for books in background which
 * the user has registered via NotificationSubscription.
 * @see NotificationRegistrationActivity#registerBookForNotifications to know how
 * a user subscribes a book for search service.
 *
 * The {@link SearchWorker} executes this background search at a minimum time interval of 15 minutes.
 * The WorkManager API does not accept time below this hence we set the time interval as 15 minutes.
 *
 * When there is at least an item in the subscription list (list containing books which the user has
 * subscribed for), the {@link SearchWorker} keeps searching in background until the user explicitly
 * clears the subscription.
 * @see MainNotificationActivity#stopNotificationsButton to understand how user stop service.
 *
 * Otherwise the work is failed i.e, stopped because there are no items to process.
 *
 * Please refer {@link NotifiableTitleResults} to know more about how and when notifications are sent.
 *
 */
public class SearchWorker extends Worker {

    private static final String WORK_TAG = "SearchTag";
    //TAG used for logging purpose i.e, to identify from which class the log message is coming from.
    private static final String TAG = "com.akhterrasool.collegelibrary.worker.SearchWorker";
    private static Map<String, NotificationItem> pendingItems = new ConcurrentHashMap<>();
    public static final int BACKGROUND_SEARCH_COMPLETED_NOTIFICATION_ID = 28397;

    /**
     * In order to start {@link SearchWorker} it must be enqueued. WorkManager enqueues a {@link SearchWorker}.
     * @param context
     * @param workerParams if any
     */
    public SearchWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    /**
     * This method causes the work to be triggered i.e, {@link SearchWorker} starts it's work.
     */
    public static void trigger() {
        //Build the request consisting the {@link SearchWorker} to be enqueued.
        //We choose {@link PeriodicWorkRequest} because we want this work i.e, background search
        //to be repeated at certain time intervals until no subscription items are left.
        PeriodicWorkRequest backgroundSearchRequest =
                new PeriodicWorkRequest.Builder(SearchWorker.class, Duration.ofMinutes(15)) //We provide 15 minutes. That is the minimum amount.
                        //You can choose to provide higher time interval i.e, 30 minutes, 1 hour, 2 hours, etc. using various methods of Duration class.
                        //Anything less than 15 minutes would be considered as 15 minutes by default.
                        //It need not execute exactly at 15 minutes. It might execute some time post 15 minutes as well.
                        .addTag(WORK_TAG) //Provide some tag to this work to identify or group related work
                        .build();//Build the periodic work request.

        //The WorkManager is responsible for starting/enqueuing the work.
        WorkManager.getInstance(App.getContext()) //This is how we get an instance of WorkManager.
                .enqueueUniquePeriodicWork(//We want to provide a unique work so that duplicate work requests are eliminated which would lead to unnecessary computation.
                        WORK_TAG, //Specify a unique work name. In this case we use the same tag as work name.
                        ExistingPeriodicWorkPolicy.REPLACE, //If multiple work requests are created, the latest request will replace the preceding request.
                        //Yielding only 1 request at the end.
                        backgroundSearchRequest);//Pass the work request you want to start.
    }

    @NonNull
    @Override
    //This method operates on a different thread.
    //Please make sure ITC is handled properly.
    public Result doWork() {
        performSearchForSubscriptions();
        if (SubscriptionUtils.atLeastOneNotificationItemExists()) {
            return success();
        } else if (!SearchService.isRunning()) {
            notifyCompletionViaNotification();
        }
        return failure();
    }

    private void performSearchForSubscriptions() {
        Set<String> subscriptionList = SubscriptionUtils.getSubscriptionItems();

        for (String itemInput: subscriptionList) {
            //Please refer the comments of obtainFromPendingItems(String itemInput).
            NotificationItem item = obtainFromPendingItems(itemInput);
            Log.i(TAG, "performSearchForSubscriptions: Sending search request.");
            NotifiableTitleResults titleSearch = new NotifiableTitleResults(item);
            Client.send(titleSearch.getRequest());//Perform search request. This will be done on another thread.

            Log.i(TAG, "performSearchForSubscriptions: Waiting for request to be processed.");
            //Wait for the request to be complete as it's being sent on another thread.
            //This block of method is being executed in one thread and the search request is sent
            //on another thread.
            //We need to wait in this manner so as to ensure proper inter thread communication.
            while (!titleSearch.isProcessed());
            Log.i(TAG, "performSearchForSubscriptions: Request has been processed.");

            if (item.isNoLongerUseful()) {
                Log.i(TAG, "performSearchForSubscriptions: Item is no longer useful.");
                remove(item);
            }
        }
    }

    /**
     * Consider an itemInput as pending and assign a new {@link NotificationItem} to it if it does not
     * exists in the pending item list.
     * {@link NotificationItem} will be useful for maintaining the notification id in order to
     * prevent too many notification being generated for a particular itemInput.
     * Example: For 'Embedded Systems' as itemInput, there is no need to show multiple notifications
     * to display it's search status. A single notification is created and that itself is reused for
     * displaying search status. If the search status changes, that particular notification is updated.
     *
     * Likewise, each itemInput will have it's own notification to display it's search status
     * and won't interfere with the status of other items.
     * @param itemInput
     * @return the item if existed otherwise the newly created notification item.
     */
    private NotificationItem obtainFromPendingItems(String itemInput) {
        pendingItems.putIfAbsent(itemInput, new NotificationItem(itemInput));
        return pendingItems.get(itemInput);
    }

    /**
     * Remove the item from pending items list as well as from Subscription list.
     */
    private void remove(NotificationItem item) {
        pendingItems.remove(item.getInput());
        SubscriptionUtils.removeItemFromSubscription(item.getInput());
    }

    private void notifyCompletionViaNotification() {
        String title = getResourceString(R.string.search_service_module_name);
        String content = getResourceString(R.string.background_search_not_running);
        SearchNotification.createNotification(title, content, BACKGROUND_SEARCH_COMPLETED_NOTIFICATION_ID);
    }

    public static void cancel() {
        WorkManager.getInstance(App.getContext()).cancelAllWorkByTag(WORK_TAG);
    }
}


