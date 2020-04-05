package com.akhterrasool.collegelibrary.util;

import android.content.Intent;
import android.widget.Toast;

import com.akhterrasool.collegelibrary.app.App;
import com.akhterrasool.collegelibrary.service.SearchService;

public class AppUtils {

    public static void showShort(String message) {
        Toast.makeText(App.getContext(), message, Toast.LENGTH_SHORT).show();
    }

    public static String getResourceString(int resourceId, Object... formatArgs) {
        return App.getContext().getResources().getString(resourceId, formatArgs);
    }

    public static void startSearchService() {
        App.getContext().startService(new Intent(App.getContext(), SearchService.class));
    }
}
