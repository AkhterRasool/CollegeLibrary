package com.akhterrasool.collegelibrary.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.akhterrasool.collegelibrary.R;
import com.akhterrasool.collegelibrary.app.App;
import com.akhterrasool.collegelibrary.notification.SearchNotification;
import com.akhterrasool.collegelibrary.util.SubscriptionUtils;
import com.akhterrasool.collegelibrary.worker.SearchWorker;

import static com.akhterrasool.collegelibrary.util.AppUtils.getResourceString;
import static com.akhterrasool.collegelibrary.util.AppUtils.showLong;
import static com.akhterrasool.collegelibrary.worker.SearchWorker.BACKGROUND_SEARCH_COMPLETED_NOTIFICATION_ID;

public class SearchService extends IntentService {

    private static final String TAG = "com.akhterrasool.collegelibrary.service.SearchService";
    private static boolean isRunning;

    public SearchService() {
        super("SearchService");
        setIntentRedelivery(true);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        App.setContext(getApplicationContext());
        Log.i(TAG, "onHandleIntent: Triggering the search worker from service.");
        SearchWorker.trigger();
        while (SubscriptionUtils.atLeastOneNotificationItemExists()) {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (App.isAppRunning()) {
            notifyCompletionViaToastMessage();
        } else {
            notifyCompletionViaNotification();
        }
        Log.i(TAG, "onHandleIntent: Service completed.");
        stopSelf();
    }

    private void notifyCompletionViaNotification() {
        String title = getResourceString(R.string.search_service_module_name);
        String content = getResourceString(R.string.background_search_not_running);
        SearchNotification.createNotification(title, content, BACKGROUND_SEARCH_COMPLETED_NOTIFICATION_ID);
    }

    private void notifyCompletionViaToastMessage() {
        getMainLooper();
        showLong(getResourceString(R.string.background_search_not_running));
        Looper.loop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isRunning = false;
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        isRunning = true;
        return super.onStartCommand(intent, flags, startId);
    }

    public static boolean isRunning() {
        return isRunning;
    }
}

