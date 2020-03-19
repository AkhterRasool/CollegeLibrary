package com.akhterrasool.collegelibrary.clientrequest;

import android.util.Log;

import com.akhterrasool.collegelibrary.activity.SearchActivity;
import com.akhterrasool.collegelibrary.util.ToastMessage;
import com.android.volley.Request;
import com.android.volley.Response;

import org.json.JSONException;
import org.json.JSONObject;

import static com.akhterrasool.collegelibrary.app.Constants.JSON_MESSAGE_KEY;

public abstract class ClientRequest<T> implements ResponseHandler<T> {

    protected String url;

    public ClientRequest(String url) {
        this.url = url;
    }

    public abstract Request getRequest();

    protected Response.ErrorListener getErrorHandler() {
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
