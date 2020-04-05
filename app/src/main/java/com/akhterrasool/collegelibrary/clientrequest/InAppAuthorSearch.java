package com.akhterrasool.collegelibrary.clientrequest;

import com.akhterrasool.collegelibrary.app.model.SearchHistoryEntry;
import com.android.volley.Response;

import org.json.JSONArray;

import static com.akhterrasool.collegelibrary.util.ActivityUtils.startResultActivity;

public class InAppAuthorSearch extends AbstractAuthorSearch {

    public InAppAuthorSearch(String authorOfBook) {
        super(authorOfBook);
    }

    @Override
    public Response.Listener<JSONArray> getResponseHandler() {
        return response ->
                extractSearchHistoryEntry(response)
                        .ifPresent(entry -> {
                            save(entry);
                            showInResultActivity(entry);
                        });
    }

    private void showInResultActivity(SearchHistoryEntry searchEntry) {
        String resultActivityTitle = "Books authored by " + searchEntry.getKey();
        startResultActivity(resultActivityTitle, searchEntry.getValue());
    }
}
