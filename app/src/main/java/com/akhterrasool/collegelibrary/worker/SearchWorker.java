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
import com.akhterrasool.collegelibrary.app.App;
import com.akhterrasool.collegelibrary.clientrequest.NotifiableTitleSearch;
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

public class SearchWorker extends Worker {

    private static final String WORK_TAG = "SearchTag";
    private static final String TAG = "SearchWorker";
    private static Map<String, NotificationItem> pendingItems = new ConcurrentHashMap<>();
    public static final int BACKGROUND_SEARCH_COMPLETED_NOTIFICATION_ID = 28397;

    public SearchWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    public static void trigger() {
        PeriodicWorkRequest backgroundSearchRequest =
                new PeriodicWorkRequest.Builder(SearchWorker.class, Duration.ofMinutes(15))
                        .addTag(WORK_TAG)
                        .build();

        WorkManager.getInstance(App.getContext())
                .enqueueUniquePeriodicWork(WORK_TAG, ExistingPeriodicWorkPolicy.REPLACE, backgroundSearchRequest);
    }

    @NonNull
    @Override
    //This method operates on a different thread.
    //Please make sure ITC is handled properly.
    public Result doWork() {
        for (int i = 0; i < 100; i++) {
            performSearchForSubscriptions();
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

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
            NotificationItem item = obtainFromPendingItems(itemInput);
            Log.i(TAG, "performSearchForSubscriptions: Sending search request.");
            NotifiableTitleSearch titleSearch = new NotifiableTitleSearch(item);
            Client.send(titleSearch.getRequest());

            Log.i(TAG, "performSearchForSubscriptions: Waiting for request to be processed.");
            while (!titleSearch.isProcessed());
            Log.i(TAG, "performSearchForSubscriptions: Request has been processed.");

            if (item.isNoLongerUseful()) {
                Log.i(TAG, "performSearchForSubscriptions: Item is no longer useful.");
                remove(item);
            }
        }
    }

    private NotificationItem obtainFromPendingItems(String itemInput) {
        pendingItems.putIfAbsent(itemInput, new NotificationItem(itemInput));
        return pendingItems.get(itemInput);
    }

    private void remove(NotificationItem item) {
        pendingItems.remove(item.getInput());
        SubscriptionUtils.removeItemFromSubscription(item.getInput());
    }

    private void notifyCompletionViaNotification() {
        String title = getResourceString(R.string.search_service_module_name);
        String content = getResourceString(R.string.background_search_not_running);
        SearchNotification.show(title, content, BACKGROUND_SEARCH_COMPLETED_NOTIFICATION_ID);
    }

    public static void cancel() {
        WorkManager.getInstance(App.getContext()).cancelAllWorkByTag(WORK_TAG);
    }
}


