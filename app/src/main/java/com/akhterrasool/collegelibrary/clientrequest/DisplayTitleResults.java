package com.akhterrasool.collegelibrary.clientrequest;

import com.akhterrasool.collegelibrary.activity.ResultActivity;
import com.akhterrasool.collegelibrary.activity.SearchActivity;
import com.akhterrasool.collegelibrary.app.model.SearchEntry;
import com.akhterrasool.collegelibrary.exception.BookNotAvailableException;
import com.android.volley.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Optional;

import static com.akhterrasool.collegelibrary.app.Constants.JSON_KEY_AUTHOR;
import static com.akhterrasool.collegelibrary.util.ActivityUtils.startResultActivity;
import static com.akhterrasool.collegelibrary.util.AppUtils.showLong;


/**
 * Class used to display results in {@link ResultActivity} when the user search.
 * This class is used when the user makes a title based search request in {@link SearchActivity}
 *
 */
public class DisplayTitleResults extends SimpleTitleSearch {

    public DisplayTitleResults(String titleName) {
        super(titleName);
    }

    @Override
    public Response.Listener<JSONObject> getResponseHandler() {
        return response -> {
            try {
                Optional<SearchEntry> optSearchEntry = extractSearchEntry(response);
                //If search entry was successfully extracted
                if (optSearchEntry.isPresent()) {
                    String author = response.getString(JSON_KEY_AUTHOR);
                    SearchEntry searchEntry = optSearchEntry.get();
                    String titleText = String.format("Locations for %s [%s]", searchEntry.getKey(), author);
                    //Save it
                    save(searchEntry);
                    //Then
                    startResultActivity(titleText, searchEntry.getValue());
                    /* and then */ markRequestAsSuccessful();
                } else {
                    //Otherwise there was some parsing error when trying to extract search entry
                    //from response as the response received was not as expected.
                    /* Hence we */ markRequestWithErrors();
                }
            } catch (JSONException e) {
                //Could not parse the author from response.
                //Seems as if the response receive is not as expected.
                e.printStackTrace();
                markRequestWithErrors();
            } catch (BookNotAvailableException e) {
                //If the book is not available simply display message.
                showLong(e.getMessage());
            }
        };
    }
}
