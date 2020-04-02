package com.akhterrasool.collegelibrary.util;

import android.content.Intent;
import android.widget.Toast;

import com.akhterrasool.collegelibrary.app.App;
import com.akhterrasool.collegelibrary.service.SearchService;
import com.akhterrasool.collegelibrary.worker.SearchWorker;

public class AppUtils {

    public static void showLong(String message) {
        Toast.makeText(App.getContext(), message, Toast.LENGTH_LONG).show();
    }

    public static String getResourceString(int resourceId, Object... formatArgs) {
        return App.getContext().getResources().getString(resourceId, formatArgs);
    }

    public static void startSearchService() {
        App.getContext().startService(new Intent(App.getContext(), SearchService.class));
    }

    public static void stopSearchService() {
        SearchWorker.cancel();
        App.getContext().stopService(new Intent(App.getContext(), SearchService.class));
    }
}
