package com.akhterrasool.collegelibrary.clientrequest;

import androidx.fragment.app.FragmentActivity;

import com.akhterrasool.collegelibrary.activity.dialog.SearchInBackgroundDialog;
import com.akhterrasool.collegelibrary.app.model.SearchHistoryEntry;
import com.akhterrasool.collegelibrary.exception.BookNotAvailableException;
import com.android.volley.Response;

import org.json.JSONException;
import org.json.JSONObject;

import static com.akhterrasool.collegelibrary.app.Constants.JSON_KEY_AUTHOR;
import static com.akhterrasool.collegelibrary.util.ActivityUtils.startResultActivity;

public class InAppTitleSearch extends AbstractTitleSearch {

    private static final String BOOK_NOT_AVAILABLE_DIALOG_TAG = "BookNotAvailableDialogTag";
    private final FragmentActivity fragmentActivity;
    private final String title;

    public InAppTitleSearch(String titleName, FragmentActivity fragmentActivity) {
        super(titleName);
        this.title = titleName;
        this.fragmentActivity = fragmentActivity;
    }

    @Override
    public Response.Listener<JSONObject> getResponseHandler() {
        return response -> {
            try {
                extractSearchHistoryEntry(response)
                        .ifPresent(entry -> {
                            save(entry);
                            showInResultActivity(response, entry);
                        });
            } catch (BookNotAvailableException e) {
                promptBackgroundSearch();
            }
        };
    }

    private void showInResultActivity(JSONObject response, SearchHistoryEntry entry) {
        String author = null;
        try {
            author = response.getString(JSON_KEY_AUTHOR);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String titleText = String.format("Locations for %s [%s]", entry.getKey(), author);
        startResultActivity(titleText, entry.getValue());
    }

    private void promptBackgroundSearch() {
        SearchInBackgroundDialog dialog = new SearchInBackgroundDialog(title);
        dialog.show(this.fragmentActivity.getSupportFragmentManager(), BOOK_NOT_AVAILABLE_DIALOG_TAG);
    }
}
