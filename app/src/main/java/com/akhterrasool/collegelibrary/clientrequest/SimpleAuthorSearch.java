package com.akhterrasool.collegelibrary.clientrequest;

import android.util.Log;

import com.akhterrasool.collegelibrary.R;
import com.akhterrasool.collegelibrary.app.model.SearchEntry;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Optional;

import static com.akhterrasool.collegelibrary.app.BookSearchType.AUTHOR;
import static com.akhterrasool.collegelibrary.app.Constants.JSON_KEY_AUTHOR;
import static com.akhterrasool.collegelibrary.app.Constants.JSON_KEY_BOOK_LOCATION;
import static com.akhterrasool.collegelibrary.app.Constants.JSON_KEY_COL;
import static com.akhterrasool.collegelibrary.app.Constants.JSON_KEY_RACK;
import static com.akhterrasool.collegelibrary.app.Constants.JSON_KEY_ROW;
import static com.akhterrasool.collegelibrary.app.Constants.JSON_KEY_TITLE;
import static com.akhterrasool.collegelibrary.app.Constants.NEW_LINE;
import static com.akhterrasool.collegelibrary.app.Constants.ROW_SEPARATOR;
import static com.akhterrasool.collegelibrary.util.AppUtils.getResourceString;


public abstract class SimpleAuthorSearch extends SearchTypeRequest<JSONArray> {

    private static final String URL = "%s/search/author/%s";
    private static final String TAG = "com.akhterrasool.collegelibrary.clientrequest.AuthorSearchType";

    public SimpleAuthorSearch(String authorName) {
        super(String.format(URL, getResourceString(R.string.root_url), authorName));
    }

    @Override
    protected Optional<SearchEntry> extractSearchEntry(JSONArray response) {

        Optional<SearchEntry> searchEntry = Optional.empty();
        try {
            String author = null;
            int numOfBooks = response.length();
            StringBuilder authorBooks = new StringBuilder("");
            for (int i = 0; i < numOfBooks; i++) {

                JSONObject book = response.getJSONObject(i);
                String title = book.getString(JSON_KEY_TITLE);
                author = book.getString(JSON_KEY_AUTHOR);
                JSONArray bookLocations = book.getJSONArray(JSON_KEY_BOOK_LOCATION);
                authorBooks.append(title);
                authorBooks.append(NEW_LINE);
                authorBooks.append(ROW_SEPARATOR);
                authorBooks.append(NEW_LINE);
                StringBuilder locations = new StringBuilder("");

                for (int j = 0; j < bookLocations.length(); j++) {
                    JSONObject bookLocation = bookLocations.getJSONObject(j);
                    int rack = bookLocation.getInt(JSON_KEY_RACK);
                    int row = bookLocation.getInt(JSON_KEY_ROW);
                    int col = bookLocation.getInt(JSON_KEY_COL);
                    locations.append(String.format("Rack - %d%nRow - %d%nColumn - %d%n", rack, row, col));
                    locations.append(NEW_LINE);
                }
                authorBooks.append(locations);
                authorBooks.append(NEW_LINE);
                Optional.of(new SearchEntry(author, authorBooks.toString(), AUTHOR));
            }
        } catch (JSONException ex) {
            Log.e(TAG, "extractSearchEntry: Could not parse response", ex);
        }
        return searchEntry;
    }

    public abstract Response.Listener<JSONArray> getResponseHandler();

    @Override
    public Request getRequest() {
        return new JsonArrayRequest(url, getResponseHandler(), getErrorHandler());
    }
}

