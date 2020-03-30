package com.akhterrasool.collegelibrary.clientrequest;

import com.akhterrasool.collegelibrary.app.model.SearchEntry;
import com.android.volley.Response;

import org.json.JSONArray;

import java.util.Optional;

import static com.akhterrasool.collegelibrary.util.ActivityUtils.startResultActivity;

public class DisplayAuthorResults extends SimpleAuthorSearch {

    public DisplayAuthorResults(String authorName) {
        super(authorName);
    }

    @Override
    public Response.Listener<JSONArray> getResponseHandler() {
        return response -> {
            Optional<SearchEntry> optSearchEntry = extractSearchEntry(response);
            if (optSearchEntry.isPresent()) {
                SearchEntry searchEntry = optSearchEntry.get();
                save(searchEntry);
                String resultActivityTitle = "Books authored by " + searchEntry.getKey();
                startResultActivity(resultActivityTitle, searchEntry.getValue());
                markRequestAsSuccessful();
            } else {
                markRequestWithErrors();
            }
        };
    }
}
