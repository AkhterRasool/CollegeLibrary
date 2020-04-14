package com.akhterrasool.collegelibrary.clientrequest;

import android.util.Log;

import com.akhterrasool.collegelibrary.R;
import com.akhterrasool.collegelibrary.app.model.SearchHistoryEntry;
import com.akhterrasool.collegelibrary.clientrequest.response.Book;
import com.akhterrasool.collegelibrary.exception.BookNotAvailableException;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Optional;

import static com.akhterrasool.collegelibrary.util.AppUtils.getResourceString;

public abstract class AbstractTitleSearch extends SaveableSearchRequest<JSONObject> {

    private static final String TAG = "AbstractTitleSearch";

    public AbstractTitleSearch(String titleName) {
        super(
                getResourceString(
                        R.string.title_search_url,
                        getResourceString(R.string.root_url),
                        titleName
                )
        );
    }

    @Override
    protected Optional<SearchHistoryEntry> extractSearchHistoryEntry(JSONObject response) throws BookNotAvailableException {
        Optional<SearchHistoryEntry> optEntry = Optional.empty();
        try {
            Book book = mapper.readValue(response.toString(), Book.class);
            if (book.getBookLocations().isEmpty()) {
                throw new BookNotAvailableException(book.getTitle());
            }
            optEntry = Optional.of(new SearchHistoryEntry(book.getTitle(), book.formatForResultActivity().toString()));
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "extractSearchHistoryEntry: Error occurred while parsing");
        }

        return optEntry;
    }

    @Override
    public abstract Response.Listener<JSONObject> getResponseHandler();

    @Override
    public Request getRequest() {
        return new JsonObjectRequest(url, null, getResponseHandler(), this.getErrorHandler());
    }
}

