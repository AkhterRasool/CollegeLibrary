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

    /**
     * Utility method to get the string from strings.xml file.
     * @param resourceId The name of the string to retrieve. Example: R.string.root_url
     * @param formatArgs The placeholders used for the string
     *                   Example: If an xml tag exists in strings.xml as follows
     *                   <code>
     *                       <string name="book_not_available">%1$s is not available.</string>
     *                   </code>
     *                   Then this method can be used as follows:
     *                  <code>
     *                      AppUtils.getResourceString(R.string.book_not_available, "Embedded Systems");
     *                  </code>
     *
     *                   "Embedded Systems" will replace '%1$s' in "%1$s is not available." message
     *                   %1$s => 1st parameter of type String.
     *                   %2$s => 2nd parameter of type String.
     *                   %1$d => 1st parameter of type Integer.
     *
     * @return the message which is formatter with placeholders.
     */
    public static String getResourceString(int resourceId, Object...formatArgs) {
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
