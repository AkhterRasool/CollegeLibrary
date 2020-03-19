package com.akhterrasool.collegelibrary.clientrequest;

import com.akhterrasool.collegelibrary.app.model.SearchEntry;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.akhterrasool.collegelibrary.app.BookSearchType.TITLE;
import static com.akhterrasool.collegelibrary.app.Constants.JSON_KEY_AUTHOR;
import static com.akhterrasool.collegelibrary.app.Constants.JSON_KEY_BOOK_LOCATION;
import static com.akhterrasool.collegelibrary.app.Constants.JSON_KEY_COL;
import static com.akhterrasool.collegelibrary.app.Constants.JSON_KEY_RACK;
import static com.akhterrasool.collegelibrary.app.Constants.JSON_KEY_ROW;
import static com.akhterrasool.collegelibrary.app.Constants.JSON_KEY_TITLE;
import static com.akhterrasool.collegelibrary.app.Constants.ROW_SEPARATOR;
import static com.akhterrasool.collegelibrary.util.ActivityUtils.startResultActivity;

public class TitleSearchType extends SearchTypeRequest<JSONObject> {


    public TitleSearchType(String url) {
        super(url);
    }

    @Override
    public Response.Listener<JSONObject> getResponseHandler() {
        return response -> {
            try {
                String title = response.getString(JSON_KEY_TITLE);
                String author = response.getString(JSON_KEY_AUTHOR);
                JSONArray bookLocations = response.getJSONArray(JSON_KEY_BOOK_LOCATION);

                StringBuilder locations = new StringBuilder("");

                for (int i = 0; i < bookLocations.length(); i++) {
                    JSONObject bookLocation = bookLocations.getJSONObject(i);
                    int rack = bookLocation.getInt(JSON_KEY_RACK);
                    int row = bookLocation.getInt(JSON_KEY_ROW);
                    int col = bookLocation.getInt(JSON_KEY_COL);
                    locations.append(String.format("Rack - %d%nRow - %d%nColumn - %d%n", rack, row, col));
                    locations.append(ROW_SEPARATOR);
                    locations.append(String.format("%n"));
                }

                String titleText = String.format("Locations for %s [%s]", title, author);
                save(new SearchEntry(title, locations.toString(), TITLE));

                startResultActivity(titleText, locations.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        };
    }

    @Override
    public Request getRequest() {
        return new JsonObjectRequest(url, null, getResponseHandler(), this.getErrorHandler());
    }
}
