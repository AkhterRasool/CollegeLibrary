package com.akhterrasool.collegelibrary.clientrequest;

import com.akhterrasool.collegelibrary.R;
import com.akhterrasool.collegelibrary.activity.ResultActivity;
import com.akhterrasool.collegelibrary.app.model.SearchEntry;
import com.akhterrasool.collegelibrary.exception.BookNotAvailableException;
import com.akhterrasool.collegelibrary.notification.SearchNotification;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Optional;

import static com.akhterrasool.collegelibrary.app.BookSearchType.TITLE;
import static com.akhterrasool.collegelibrary.app.Constants.JSON_KEY_BOOK_LOCATION;
import static com.akhterrasool.collegelibrary.app.Constants.JSON_KEY_COL;
import static com.akhterrasool.collegelibrary.app.Constants.JSON_KEY_RACK;
import static com.akhterrasool.collegelibrary.app.Constants.JSON_KEY_ROW;
import static com.akhterrasool.collegelibrary.app.Constants.JSON_KEY_TITLE;
import static com.akhterrasool.collegelibrary.app.Constants.ROW_SEPARATOR;
import static com.akhterrasool.collegelibrary.util.AppUtils.getResourceString;

/**
 * Indicates that this class is used for search books based on their titles only.
 * This class only performs search and does not focuses on how the results are to be handled
 * hence abstract (incomplete) implementation.
 *
 * If results are required to be displayed in {@link ResultActivity} then
 * @see DisplayTitleResults
 * If results are required to be displayed via {@link SearchNotification} then
 * @see NotifiableTitleResults
 *
 */
public abstract class SimpleTitleSearch extends SearchTypeRequest<JSONObject> {

    private static final String URL = "%s/search/title/%s";
    public SimpleTitleSearch(String titleName) {
        //Format the URL as "http://10.0.2.2:8080/CollegeLibraryServer/search/title/operating systems concepts
        super(String.format(URL, getResourceString(R.string.root_url), titleName));
    }

    @Override
    protected Optional<SearchEntry> extractSearchEntry(JSONObject response) throws BookNotAvailableException {
        //Optional is used to check if the search entry is extractable or not.
        Optional<SearchEntry> searchEntry = Optional.empty();
        try {
            String title = response.getString(JSON_KEY_TITLE);
            JSONArray bookLocations = response.getJSONArray(JSON_KEY_BOOK_LOCATION);

            StringBuilder locations = new StringBuilder("");

            //If there are no location, then throw exception.
            //It means that search entry is extractable i.e, the response received is as expected
            //only thing is that there are no locations. Hence the exception.
            if (bookLocations.length() == 0) {
                throw new BookNotAvailableException(title);
            }
            for (int i = 0; i < bookLocations.length(); i++) {
                JSONObject bookLocation = bookLocations.getJSONObject(i);
                int rack = bookLocation.getInt(JSON_KEY_RACK);
                int row = bookLocation.getInt(JSON_KEY_ROW);
                int col = bookLocation.getInt(JSON_KEY_COL);
                locations.append(String.format("Rack - %d%nRow - %d%nColumn - %d%n", rack, row, col));
                locations.append(ROW_SEPARATOR);
                locations.append(String.format("%n"));
            }

            searchEntry = Optional.of(new SearchEntry(title, locations.toString(), TITLE));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return searchEntry;
    }

    @Override
    public abstract Response.Listener<JSONObject> getResponseHandler();

    @Override
    public Request getRequest() {
        return new JsonObjectRequest(url, null, getResponseHandler(), this.getErrorHandler());
    }
}

