package com.akhterrasool.collegelibrary.clientrequest;

import android.util.Log;

import com.akhterrasool.collegelibrary.R;
import com.akhterrasool.collegelibrary.activity.SearchActivity;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import static com.akhterrasool.collegelibrary.app.Constants.JSON_MESSAGE_KEY;
import static com.akhterrasool.collegelibrary.util.AppUtils.getResourceString;
import static com.akhterrasool.collegelibrary.util.AppUtils.showShort;

public abstract class ClientRequest<T> implements ResponseHandler<T> {

    private static final String TAG = "clientrequest.ClientRequest";
    private static final String CANNOT_CONNECT_TO_SERVER = getResourceString(R.string.could_not_connect_to_server);
    protected String url;

    public ClientRequest(String url) {
        this.url = url;
    }

    public abstract Request getRequest();

    protected Response.ErrorListener getErrorHandler() {
        Log.e(TAG, "getErrorHandler: Error occurred. Using Default error handler");
        Response.ErrorListener errorListener = error -> {
            String message = null;
            try {
                message = getConnectionFailedMessage(error);
                if (message.isEmpty()) {
                    JSONObject errorData = new JSONObject(new String(error.networkResponse.data));
                    message = errorData.get(JSON_MESSAGE_KEY).toString();
                }
            } catch (JSONException exception) {
                message = exception.getMessage();
            }
            Log.e(SearchActivity.class.getName(), message);
            showShort(message);
        };
        return errorListener;
    }

    protected String getConnectionFailedMessage(VolleyError error) {
        return (error == null || error.networkResponse == null || error.networkResponse.data == null)
                ? CANNOT_CONNECT_TO_SERVER : "";
    }
}
