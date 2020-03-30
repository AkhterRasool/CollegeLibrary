package com.akhterrasool.collegelibrary.clientrequest;

import com.akhterrasool.collegelibrary.app.model.SearchEntry;
import com.akhterrasool.collegelibrary.exception.BookNotAvailableException;
import com.android.volley.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Optional;

import static com.akhterrasool.collegelibrary.app.Constants.JSON_KEY_AUTHOR;
import static com.akhterrasool.collegelibrary.util.ActivityUtils.startResultActivity;
import static com.akhterrasool.collegelibrary.util.AppUtils.showLong;

public class DisplayTitleResults extends SimpleTitleSearch {

    public DisplayTitleResults(String titleName) {
        super(titleName);
    }

    @Override
    public Response.Listener<JSONObject> getResponseHandler() {
        return response -> {
            try {
                Optional<SearchEntry> optSearchEntry = extractSearchEntry(response);
                if (optSearchEntry.isPresent()) {
                    String author = response.getString(JSON_KEY_AUTHOR);
                    SearchEntry searchEntry = optSearchEntry.get();
                    String titleText = String.format("Locations for %s [%s]", searchEntry.getKey(), author);
                    save(searchEntry);
                    startResultActivity(titleText, searchEntry.getValue());
                    markRequestAsSuccessful();
                } else {
                    markRequestWithErrors();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                markRequestWithErrors();
            } catch (BookNotAvailableException e) {
                showLong(e.getMessage());
            }
        };
    }
}
