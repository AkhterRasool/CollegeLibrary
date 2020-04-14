package com.akhterrasool.collegelibrary.clientrequest;

import android.util.Log;

import com.akhterrasool.collegelibrary.R;
import com.akhterrasool.collegelibrary.app.model.SearchHistoryEntry;
import com.akhterrasool.collegelibrary.clientrequest.response.Author;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Optional;

import static com.akhterrasool.collegelibrary.util.AppUtils.getResourceString;


public abstract class AbstractAuthorSearch extends SaveableSearchRequest<JSONObject> {

    private static final String TAG = "AuthorSearchType";

    public AbstractAuthorSearch(String authorName) {
        super(
                getResourceString(
                        R.string.author_search_url,
                        getResourceString(R.string.root_url),
                        authorName
                )
        );
    }

    @Override
    protected Optional<SearchHistoryEntry> extractSearchHistoryEntry(JSONObject response) {

        Optional<SearchHistoryEntry> searchEntry = Optional.empty();
        try {
            long start = System.currentTimeMillis();
            Author author = mapper.readValue(response.toString(), Author.class);
            searchEntry = Optional.of(new SearchHistoryEntry(author.getName(), author.formatForResultActivity().toString()));
            long timeTaken = System.currentTimeMillis() - start;
            Log.i(TAG, "extractSearchHistoryEntry: Time Taken: " + timeTaken);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return searchEntry;
    }

    @Override
    public Request getRequest() {
        return new JsonObjectRequest(url, null, getResponseHandler(), getErrorHandler());
    }
}

