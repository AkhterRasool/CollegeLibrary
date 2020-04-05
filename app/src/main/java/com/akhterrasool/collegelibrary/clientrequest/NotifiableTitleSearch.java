package com.akhterrasool.collegelibrary.clientrequest;

import android.util.Log;

import com.akhterrasool.collegelibrary.R;
import com.akhterrasool.collegelibrary.app.model.SearchHistoryEntry;
import com.akhterrasool.collegelibrary.exception.BookNotAvailableException;
import com.akhterrasool.collegelibrary.notification.SearchNotification;
import com.akhterrasool.collegelibrary.worker.NotificationItem;
import com.android.volley.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Optional;

import static com.akhterrasool.collegelibrary.app.Constants.JSON_MESSAGE_KEY;
import static com.akhterrasool.collegelibrary.util.AppUtils.getResourceString;

public class NotifiableTitleSearch extends AbstractTitleSearch {

    private static final String TAG = "clientrequest.NotifiableTitleResults";
    private final NotificationItem notificationItem;
    private boolean isProcessed;

    public NotifiableTitleSearch(NotificationItem item) {
        super(item.getInput());
        this.notificationItem = item;
        isProcessed = false;
    }

    @Override
    public Response.Listener<JSONObject> getResponseHandler() {
        return response -> {
            Optional<SearchHistoryEntry> optSearchEntry = null;
            try {
                optSearchEntry = extractSearchHistoryEntry(response);
                if (optSearchEntry.isPresent()) {
                    Log.i(TAG, "getResponseHandler: Search entry extracted preparing notification.");
                    SearchHistoryEntry searchEntry = optSearchEntry.get();
                    save(searchEntry);
                    String resultActivityTitle = "Search results for " + searchEntry.getKey();
                    String popUpMessage = getResourceString(R.string.search_results_saved);
                    SearchNotification.showNotificationNavigatingToResultActivity(
                            getResourceString(R.string.notification_search_status_title, this.notificationItem.getInput()),
                            getResourceString(R.string.notification_content_search_completed),
                            resultActivityTitle,
                            searchEntry.getValue(),
                            popUpMessage,
                            this.notificationItem.getNotificationId());
                    Log.i(TAG, "getResponseHandler: Displayed notification.");
                    notificationItem.setNoLongerUseful(true);
                } else {
                    this.notificationItem.setNoLongerUseful(true);
                }
            } catch (BookNotAvailableException e) {
                // Book is still not available
                // Ignore it for now.
                // Retry it later.
            } finally {
                this.isProcessed = true;
            }
        };
    }

    @Override
    protected Response.ErrorListener getErrorHandler() {
        return error -> {
            Log.e(TAG, "getErrorHandler: Error occurred while processing request");
            try {
                if (getConnectionFailedMessage(error).isEmpty()) {
                    JSONObject errorData = new JSONObject(new String(error.networkResponse.data));
                    String errorMessage = errorData.get(JSON_MESSAGE_KEY).toString();
                    this.notificationItem.setNoLongerUseful(true);
                    SearchNotification.show(
                            getResourceString(R.string.notification_search_status_title, this.notificationItem.getInput()),
                            errorMessage,
                            this.notificationItem.getNotificationId());
                }
            } catch (JSONException exception) {
                exception.printStackTrace();
                Log.e(TAG, "getErrorHandler: Error occurred while parsing response", exception);
            } finally {
                this.isProcessed = true;
            }

        };
    }

    public boolean isProcessed() {
        return isProcessed;
    }
}
