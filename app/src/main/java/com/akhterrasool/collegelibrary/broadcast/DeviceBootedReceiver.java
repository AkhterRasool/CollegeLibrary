package com.akhterrasool.collegelibrary.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.akhterrasool.collegelibrary.app.App;
import com.akhterrasool.collegelibrary.worker.SearchWorker;

/**
 * This is a receiver which is executed when the device is booted up.
 */
public class DeviceBootedReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        App.setContext(context.getApplicationContext());
        SearchWorker.trigger();
    }
}
