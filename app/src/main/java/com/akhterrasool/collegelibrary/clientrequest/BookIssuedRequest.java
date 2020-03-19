package com.akhterrasool.collegelibrary.clientrequest;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.akhterrasool.collegelibrary.app.Constants.NEW_LINE;
import static com.akhterrasool.collegelibrary.app.Constants.ROW_SEPARATOR;
import static com.akhterrasool.collegelibrary.util.ActivityUtils.startResultActivity;

public class BookIssuedRequest extends ClientRequest<JSONObject> {

    public BookIssuedRequest(String url) {
        super(url);
    }

    @Override
    public Request getRequest() {
        return new JsonObjectRequest(url, null, getResponseHandler(), getErrorHandler());
    }

    @Override
    public Response.Listener<JSONObject> getResponseHandler() {
        return response -> {
            StringBuffer issued = new StringBuffer();

            try {
                String rollNumber = response.getString("rollNumber");
                double fineAmount = response.getDouble("fineAmount");

                JSONArray booksIssued = response.getJSONArray("booksIssued");

                for (int i = 0; i < booksIssued.length(); i++) {
                    JSONObject book = booksIssued.getJSONObject(i);
                    String title = book.getString("title");
                    issued.append(NEW_LINE);
                    issued.append(String.format("%d) %s", i + 1, title));
                    issued.append(NEW_LINE);
                    issued.append(ROW_SEPARATOR);
                }

                issued.append(NEW_LINE);

                issued.append("Total fine amount " + fineAmount + " Rs/-");

                String title = "Books issued by student [" + rollNumber + "]";

                startResultActivity(title, issued.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        };
    }
}
