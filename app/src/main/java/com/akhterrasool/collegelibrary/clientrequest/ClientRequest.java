package com.akhterrasool.collegelibrary.clientrequest;

import android.util.Log;

import com.akhterrasool.collegelibrary.activity.SearchActivity;
import com.android.volley.Request;
import com.android.volley.Response;

import org.json.JSONException;
import org.json.JSONObject;

import static com.akhterrasool.collegelibrary.app.Constants.JSON_MESSAGE_KEY;
import static com.akhterrasool.collegelibrary.util.AppUtils.showLong;

public abstract class ClientRequest<T> implements ResponseHandler<T> {

    private static final String TAG = "com.akhterrasool.collegelibrary.clientrequest.ClientRequest";
    protected String url;
    private boolean isProcessed = false;
    private boolean hasErrors = false;
    private boolean isSuccess = false;

    public ClientRequest(String url) {
        this.url = url;
    }

    protected void markRequestWithErrors() {
        Log.e(TAG, "markRequestWithErrors: Marking request with errors");
        hasErrors = true;
        isSuccess = false;
        isProcessed = true;
    }


    protected void markRequestAsSuccessful() {
        Log.i(TAG, "markRequestWithErrors: Marking request as successful");
        hasErrors = false;
        isSuccess = true;
        isProcessed = true;
    }

    public boolean hasErrors() {
        return hasErrors;
    }

    public boolean isProcessed() {
        return this.isProcessed;
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
            showLong(message);
            markRequestWithErrors();
        };
        return errorListener;
    }
}
