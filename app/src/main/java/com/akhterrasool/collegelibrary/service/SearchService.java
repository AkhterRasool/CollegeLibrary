package com.akhterrasool.collegelibrary.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.akhterrasool.collegelibrary.R;
import com.akhterrasool.collegelibrary.app.App;
import com.akhterrasool.collegelibrary.notification.SearchNotification;
import com.akhterrasool.collegelibrary.util.NotificationUtils;
import com.akhterrasool.collegelibrary.worker.SearchWorker;

import static com.akhterrasool.collegelibrary.util.AppUtils.getResourceString;
import static com.akhterrasool.collegelibrary.util.AppUtils.showLong;

public class SearchService extends IntentService {

    private static final String TAG = "com.akhterrasool.collegelibrary.service.SearchService";
    private static final int SERVICE_COMPLETE_STATUS_NOTIFICATION_ID = 28397;

    public SearchService() {
        super("SearchService");
        setIntentRedelivery(true);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        App.setContext(getApplicationContext());
        Log.i(TAG, "onHandleIntent: Triggering the search worker from service.");
        SearchWorker.trigger();
        while (NotificationUtils.atLeastOneNotificationItemExists()) {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (App.isAppRunning()) {
            notifyViaToastMessage();
        } else {
            notifyViaNotification();
        }
        Log.i(TAG, "onHandleIntent: Service completed.");
        stopSelf();
    }

    private void notifyViaNotification() {
        String title = getResourceString(R.string.search_service_module_name);
        String content = getResourceString(R.string.service_successfully_stopped);
        SearchNotification.createNotification(title, content, SERVICE_COMPLETE_STATUS_NOTIFICATION_ID);
    }

    private void notifyViaToastMessage() {
        getMainLooper();
        showLong(getResourceString(R.string.service_successfully_stopped));
        Looper.loop();
    }
}

