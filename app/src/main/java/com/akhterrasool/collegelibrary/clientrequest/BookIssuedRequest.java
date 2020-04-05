package com.akhterrasool.collegelibrary.clientrequest;

import com.akhterrasool.collegelibrary.R;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.akhterrasool.collegelibrary.app.Constants.JSON_KEY_BOOKS_ISSUED;
import static com.akhterrasool.collegelibrary.app.Constants.JSON_KEY_FINE_AMOUNT;
import static com.akhterrasool.collegelibrary.app.Constants.JSON_KEY_ROLL_NUMBER;
import static com.akhterrasool.collegelibrary.app.Constants.JSON_KEY_TITLE;
import static com.akhterrasool.collegelibrary.app.Constants.NEW_LINE;
import static com.akhterrasool.collegelibrary.app.Constants.ROW_SEPARATOR;
import static com.akhterrasool.collegelibrary.util.ActivityUtils.startResultActivity;
import static com.akhterrasool.collegelibrary.util.AppUtils.getResourceString;

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
                String rollNumber = response.getString(JSON_KEY_ROLL_NUMBER);
                double fineAmount = response.getDouble(JSON_KEY_FINE_AMOUNT);

                JSONArray booksIssued = response.getJSONArray(JSON_KEY_BOOKS_ISSUED);

                for (int i = 0; i < booksIssued.length(); i++) {
                    JSONObject book = booksIssued.getJSONObject(i);
                    String title = book.getString(JSON_KEY_TITLE);
                    issued.append(NEW_LINE);
                    issued.append(String.format("%d) %s", i + 1, title));
                    issued.append(NEW_LINE);
                    issued.append(ROW_SEPARATOR);
                }

                issued.append(NEW_LINE);
                issued.append(NEW_LINE);

                String fineAmountMessage = getResourceString(R.string.fine_amount_message, fineAmount, "Rs/-");
                issued.append(fineAmountMessage);
                String title = getResourceString(R.string.book_issued_result_activity_title, rollNumber);

                startResultActivity(title, issued.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        };
    }
}
