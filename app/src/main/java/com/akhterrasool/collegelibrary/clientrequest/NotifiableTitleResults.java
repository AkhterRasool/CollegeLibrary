package com.akhterrasool.collegelibrary.clientrequest;

import android.util.Log;

import com.akhterrasool.collegelibrary.R;
import com.akhterrasool.collegelibrary.app.model.SearchEntry;
import com.akhterrasool.collegelibrary.exception.BookNotAvailableException;
import com.akhterrasool.collegelibrary.notification.SearchNotification;
import com.akhterrasool.collegelibrary.worker.NotificationItem;
import com.android.volley.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;
import java.util.Optional;

import static com.akhterrasool.collegelibrary.app.Constants.JSON_MESSAGE_KEY;
import static com.akhterrasool.collegelibrary.util.AppUtils.getResourceString;

public class NotifiableTitleResults extends SimpleTitleSearch {

    private static final String TAG = "com.akhterrasool.collegelibrary.clientrequest.NotifiableTitleResults";
    private final String title;
    private final NotificationItem notificationItem;

    public NotifiableTitleResults(NotificationItem item) {
        super(item.getInput());
        this.notificationItem = item;
        this.title = item.getInput();
    }

    @Override
    public Response.Listener<JSONObject> getResponseHandler() {
        return response -> {
            Optional<SearchEntry> optSearchEntry = null;
            try {
                optSearchEntry = extractSearchEntry(response);
                if (optSearchEntry.isPresent()) {
                    Log.i(TAG, "getResponseHandler: Search entry extracted preparing notification.");
                    SearchEntry searchEntry = optSearchEntry.get();
                    save(searchEntry);
                    String resultActivityTitle = "Search results for " + searchEntry.getKey();
                    String popUpMessage = getResourceString(R.string.search_results_saved);
                    showNotification(
                            getResourceString(R.string.notification_content_search_completed),
                            resultActivityTitle,
                            searchEntry.getValue(),
                            popUpMessage);
                    Log.i(TAG, "getResponseHandler: Displayed notification.");
                    notificationItem.setNoLongerUseful(true);
                    markRequestAsSuccessful();
                } else {
                    this.notificationItem.setNoLongerUseful(true);
                    markRequestWithErrors();
                }
            } catch (BookNotAvailableException e) {
                markRequestAsSuccessful();
            }
        };
    }

    @Override
    protected Response.ErrorListener getErrorHandler() {
        return error -> {
            Log.e(TAG, "getErrorHandler: Error occurred while processing request", error.fillInStackTrace());

            String errorMessage = null;
            try {
                Objects.requireNonNull(error);
                Objects.requireNonNull(error.networkResponse);
                Objects.requireNonNull(error.networkResponse.data);
                JSONObject errorData = new JSONObject(new String(error.networkResponse.data));
                errorMessage = errorData.get(JSON_MESSAGE_KEY).toString();
            } catch (JSONException | NullPointerException e) {
                e.printStackTrace();
                Log.e(TAG, "getErrorHandler: Error occurred while parsing response", e);
                errorMessage = e.getMessage();
            }
            showNotification(errorMessage);
            this.notificationItem.setNoLongerUseful(true);
            markRequestWithErrors();
        };
    }

    private void showNotification(String textContent) {
        SearchNotification.createNotification(
                getResourceString(R.string.notification_search_status_title, this.notificationItem.getInput()),
                textContent,
                this.notificationItem.getNotificationId());
    }

    private void showNotification(String textContent,
                                  String resultActivityTitle,
                                  String resultActivityBody,
                                  String toastMessage) {
        SearchNotification.createNotificationWithResultActivity(
                getResourceString(R.string.notification_search_status_title, this.notificationItem.getInput()),
                textContent,
                resultActivityTitle,
                resultActivityBody,
                toastMessage,
                this.notificationItem.getNotificationId());
    }

}
