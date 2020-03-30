package com.akhterrasool.collegelibrary.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.Nullable;

import com.akhterrasool.collegelibrary.app.App;
import com.akhterrasool.collegelibrary.util.NotificationUtils;
import com.akhterrasool.collegelibrary.worker.SearchWorker;

public class SearchService extends IntentService {

    private static final String TAG = "com.akhterrasool.collegelibrary.service.SearchService";

    public SearchService() {
        super("SearchService");
        setIntentRedelivery(true);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.i(TAG, "onHandleIntent: Triggering the search worker from service.");
        SearchWorker.trigger();
        while (NotificationUtils.atLeastOneNotificationItemExists()) {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        stopSelf();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        App.setContext(getApplicationContext());
    }
}

