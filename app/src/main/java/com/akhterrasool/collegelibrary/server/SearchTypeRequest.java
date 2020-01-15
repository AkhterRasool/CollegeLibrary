package com.akhterrasool.collegelibrary.server;

import android.util.Log;

import com.akhterrasool.collegelibrary.activity.SearchActivity;
import com.akhterrasool.collegelibrary.util.ToastMessage;
import com.android.volley.Request;
import com.android.volley.Response;

import org.json.JSONException;
import org.json.JSONObject;

import static com.akhterrasool.collegelibrary.app.Constants.JSON_MESSAGE_KEY;

public interface SearchTypeRequest<T, R extends Request> {

    Response.Listener<T> getResponseListener();

    Request getRequest();

    default Response.ErrorListener getErrorListener() {
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
