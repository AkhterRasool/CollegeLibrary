package com.akhterrasool.collegelibrary.clientrequest;

import com.akhterrasool.collegelibrary.R;
import com.akhterrasool.collegelibrary.clientrequest.response.StudentBookIssues;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.io.IOException;

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
            try {
                StudentBookIssues studentBookIssues = mapper.readValue(response.toString(), StudentBookIssues.class);
                String title = getResourceString(R.string.book_issued_result_activity_title, studentBookIssues.getRollNumber());
                startResultActivity(title, studentBookIssues.formatForResultActivity().toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        };
    }
}
