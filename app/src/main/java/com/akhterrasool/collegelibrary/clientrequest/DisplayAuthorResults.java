package com.akhterrasool.collegelibrary.clientrequest;

import com.akhterrasool.collegelibrary.activity.ResultActivity;
import com.akhterrasool.collegelibrary.activity.SearchActivity;
import com.akhterrasool.collegelibrary.app.model.SearchEntry;
import com.android.volley.Response;

import org.json.JSONArray;

import java.util.Optional;

import static com.akhterrasool.collegelibrary.util.ActivityUtils.startResultActivity;

/**
 * Class used to display results in {@link ResultActivity} when the user search.
 * This class is used when the user makes a author based search request in {@link SearchActivity}
 */
public class DisplayAuthorResults extends SimpleAuthorSearch {

    public DisplayAuthorResults(String authorName) {
        super(authorName);
    }

    @Override
    public Response.Listener<JSONArray> getResponseHandler() {
        return response -> {
            Optional<SearchEntry> optSearchEntry = extractSearchEntry(response);
            //If search entry was successfully extracted
            if (optSearchEntry.isPresent()) {
                //then
                SearchEntry searchEntry = optSearchEntry.get();
                //Save it
                save(searchEntry);
                String resultActivityTitle = "Books authored by " + searchEntry.getKey();
                //Start result activity.
                startResultActivity(resultActivityTitle, searchEntry.getValue());
                /* And then */ markRequestAsSuccessful();
            } else {
                //Otherwise there was some parsing error when trying to extract search entry
                //from response as the response received was not as expected.
                /* Hence we */ markRequestWithErrors();
            }
        };
    }
}
