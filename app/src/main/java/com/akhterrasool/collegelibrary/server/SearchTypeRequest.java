package com.akhterrasool.collegelibrary.server;

import android.content.SharedPreferences;
import android.util.Log;

import com.akhterrasool.collegelibrary.activity.SearchActivity;
import com.akhterrasool.collegelibrary.app.App;
import com.akhterrasool.collegelibrary.app.model.SearchEntry;
import com.akhterrasool.collegelibrary.util.ToastMessage;
import com.android.volley.Request;
import com.android.volley.Response;

import org.json.JSONException;
import org.json.JSONObject;

import static com.akhterrasool.collegelibrary.app.Constants.JSON_MESSAGE_KEY;

public abstract class SearchTypeRequest<T> implements ResponseHandler {

    abstract Request getRequest();

    protected void save(SearchEntry searchEntry) {
        SharedPreferences.Editor editor = App.getSearchHistoryPreference().edit();
        editor.putString(searchEntry.getKey(), searchEntry.toString());
        editor.apply();
    }

    protected Response.ErrorListener getErrorListener() {
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
        return errorListener;
    }
}
