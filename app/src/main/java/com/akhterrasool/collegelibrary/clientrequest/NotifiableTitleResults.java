package com.akhterrasool.collegelibrary.clientrequest;

import android.util.Log;

import com.akhterrasool.collegelibrary.R;
import com.akhterrasool.collegelibrary.activity.ResultActivity;
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

/**
 * This class is used to display notification when search request is performed and not start
 * {@link ResultActivity}
 *
 * @see DisplayTitleResults before proceeding with this class.
 */
public class NotifiableTitleResults extends SimpleTitleSearch {

    //TAG used for logging purpose i.e, to identify from which class the log message is coming from.
    private static final String TAG = "com.akhterrasool.collegelibrary.clientrequest.NotifiableTitleResults";
    private final String title;
    //Use this item to display in notification
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
                    //The search is performed, now focus on displaying the notification
                    String resultActivityTitle = "Search results for " + searchEntry.getKey();
                    String popUpMessage = getResourceString(R.string.search_results_saved);
                    //Refer docs of this method: showNotification
                    showNotification(
                            getResourceString(R.string.notification_content_search_completed),
                            resultActivityTitle,
                            searchEntry.getValue(),
                            popUpMessage); //Inform the user that the results were saved.
                    Log.i(TAG, "getResponseHandler: Displayed notification.");
                    notificationItem.setNoLongerUseful(true); //Because search successful.
                    markRequestAsSuccessful();
                } else {
                    this.notificationItem.setNoLongerUseful(true); //Because invalid input passed by user, hence remove it.
                    markRequestWithErrors();
                }
            } catch (BookNotAvailableException e) {
                markRequestAsSuccessful();
            }
        };
    }

    /**
     * In case of any error occurring during backgrond search, send a notification to keep the user
     * informed.
     * @return
     */
    @Override
    protected Response.ErrorListener getErrorHandler() {
        return error -> {
            Log.e(TAG, "getErrorHandler: Error occurred while processing request", error.fillInStackTrace());

            String errorMessage = null;
            try {
                //Make sure there is some error message, otherwise throw NullPointerException
                //if no error message is there.
                Objects.requireNonNull(error);
                Objects.requireNonNull(error.networkResponse);
                Objects.requireNonNull(error.networkResponse.data);
                //Otherwise parse the error message.
                JSONObject errorData = new JSONObject(new String(error.networkResponse.data));
                errorMessage = errorData.get(JSON_MESSAGE_KEY).toString();
            } catch (JSONException | NullPointerException e) {
                e.printStackTrace();
                Log.e(TAG, "getErrorHandler: Error occurred while parsing response", e);
                errorMessage = e.getMessage();
            }
            showNotification(errorMessage);
            this.notificationItem.setNoLongerUseful(true); //Because this input gives errors.
            markRequestWithErrors();
        };
    }

    /**
     * Show notification with already defined title.
     * Only focuses on setting the content.
     * @param textContent
     */
    private void showNotification(String textContent) {
        SearchNotification.createNotification(
                getResourceString(R.string.notification_search_status_title, this.notificationItem.getInput()), //This is the title
                textContent,
                this.notificationItem.getNotificationId());
    }

    /**
     * Show notification with already defined title but launch {@link ResultActivity} when
     * user clicks on it.
     * @param textContent
     * @param resultActivityTitle
     * @param resultActivityBody
     * @param toastMessage
     */
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
