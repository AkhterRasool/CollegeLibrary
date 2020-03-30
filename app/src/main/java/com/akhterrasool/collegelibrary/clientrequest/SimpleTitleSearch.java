package com.akhterrasool.collegelibrary.clientrequest;

import com.akhterrasool.collegelibrary.R;
import com.akhterrasool.collegelibrary.app.model.SearchEntry;
import com.akhterrasool.collegelibrary.exception.BookNotAvailableException;
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

public abstract class SimpleTitleSearch extends SearchTypeRequest<JSONObject> {

    private static final String URL = "%s/search/title/%s";
    public SimpleTitleSearch(String titleName) {
        super(String.format(URL, getResourceString(R.string.root_url), titleName));
    }

    @Override
    protected Optional<SearchEntry> extractSearchEntry(JSONObject response) throws BookNotAvailableException {
        Optional<SearchEntry> searchEntry = Optional.empty();
        try {
            String title = response.getString(JSON_KEY_TITLE);
            JSONArray bookLocations = response.getJSONArray(JSON_KEY_BOOK_LOCATION);

            StringBuilder locations = new StringBuilder("");

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

