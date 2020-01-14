package com.akhterrasool.collegelibrary.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.akhterrasool.collegelibrary.R;
import com.akhterrasool.collegelibrary.app.App;
import com.akhterrasool.collegelibrary.util.Client;
import com.akhterrasool.collegelibrary.util.ToastMessage;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SearchActivity extends AppCompatActivity {

    private static final String JSON_MESSAGE_KEY = "message";
    private static final String JSON_KEY_TITLE = "title";
    private static final String JSON_KEY_AUTHOR = "author";
    private static final String JSON_KEY_BOOK_LOCATION = "bookLocation";
    private static final String JSON_KEY_RACK = "rack";
    private static final String JSON_KEY_ROW = "row";
    private static final String JSON_KEY_COL = "col";
    public static final String ROW_SEPARATOR = "------------------------------------";

    private EditText bookTextField;
    private Button searchButton;
    private Button clearButton;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));

        bookTextField = findViewById(R.id.book_text_field);
        searchButton = findViewById(R.id.search_button);
        clearButton = findViewById(R.id.clear_button);

        searchButton.setOnClickListener(view -> {
            String book = bookTextField.getText().toString();
            if (!book.isEmpty()) {
                sendSearchRequest(book);
            }
        });

        clearButton.setOnClickListener(view -> {
            bookTextField.setText(R.string.empty_string);
            Log.i(SearchActivity.class.getName(), "Book Text field has been cleared.");
        });
    }

    private void sendSearchRequest(String book) {
        Log.i(SearchActivity.class.getName(), "User has entered '" + book + "'");
        String url =
                String.format("%s/book/%s", getResources().getString(R.string.root_url), book);
        Log.i(SearchActivity.class.getName(), "Navigating to " + url);

        Response.Listener<JSONObject> responseListener = response -> {
            //Send the response to a ResultActivity
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

                String message = String.format("%s [%s] is located in%n%n%s", title, author, locations);
                Intent resultIntent = new Intent(App.getContext(), ResultActivity.class);
                resultIntent.putExtra(ResultActivity.RESPONSE_TEXT, message);
                startActivity(resultIntent);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        };

        Response.ErrorListener errorListener = error -> {
            String message = null;
            try {
                JSONObject errorData = new JSONObject(new String(error.networkResponse.data));
                message = errorData.get(JSON_MESSAGE_KEY).toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.e(SearchActivity.class.getName(), message);
            ToastMessage.showLong(message);
        };

        JsonObjectRequest searchRequest =
                new JsonObjectRequest(url, null, responseListener, errorListener);
        Client.send(searchRequest);
    }
}
