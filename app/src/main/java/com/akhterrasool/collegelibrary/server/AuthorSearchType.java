package com.akhterrasool.collegelibrary.server;

import android.content.Intent;

import com.akhterrasool.collegelibrary.activity.ResultActivity;
import com.akhterrasool.collegelibrary.app.App;
import com.akhterrasool.collegelibrary.app.model.SearchEntry;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.akhterrasool.collegelibrary.app.BookSearchType.AUTHOR;
import static com.akhterrasool.collegelibrary.app.Constants.JSON_KEY_AUTHOR;
import static com.akhterrasool.collegelibrary.app.Constants.JSON_KEY_BOOK_LOCATION;
import static com.akhterrasool.collegelibrary.app.Constants.JSON_KEY_COL;
import static com.akhterrasool.collegelibrary.app.Constants.JSON_KEY_RACK;
import static com.akhterrasool.collegelibrary.app.Constants.JSON_KEY_ROW;
import static com.akhterrasool.collegelibrary.app.Constants.JSON_KEY_TITLE;
import static com.akhterrasool.collegelibrary.app.Constants.NEW_LINE;
import static com.akhterrasool.collegelibrary.app.Constants.ROW_SEPARATOR;


public class AuthorSearchType extends SearchTypeRequest<JSONArray> {
    private final String url;

    public AuthorSearchType(String url) {
        this.url = url;
    }

    @Override
    public Response.Listener<JSONArray> getResponseHandler() {
        return response -> {

            String author = null;

            int numOfBooks = response.length();
            StringBuilder authorBooks = new StringBuilder("");

            /*
                Sample Response Output:
                Operating systems
                ---------------
                Rack - 1
                Row - 2
                Column - 10

                Rack - 11
                Row - 22
                Column - 1

                Database System
                ---------------
                Rack - 1
                Row - 2
                Column - 10
             */

            for (int i = 0; i < numOfBooks; i++) {
                try {
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
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            String title = String.format("Books authored by %s", author);
            save(new SearchEntry(author, authorBooks.toString(), AUTHOR));
            Intent resultIntent = new Intent(App.getContext(), ResultActivity.class);
            resultIntent.putExtra(ResultActivity.RESULT_ACTIVITY_RESPONSE_BODY, authorBooks.toString());
            resultIntent.putExtra(ResultActivity.RESULT_ACTIVITY_TITLE, title);
            App.getContext().startActivity(resultIntent);
        };
    }

    @Override
    public Request getRequest() {
        return new JsonArrayRequest(url, getResponseHandler(), getErrorListener());
    }
}
