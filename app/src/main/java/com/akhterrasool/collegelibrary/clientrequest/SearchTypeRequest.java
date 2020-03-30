package com.akhterrasool.collegelibrary.clientrequest;

import android.content.SharedPreferences;

import com.akhterrasool.collegelibrary.app.App;
import com.akhterrasool.collegelibrary.app.model.SearchEntry;
import com.akhterrasool.collegelibrary.exception.BookNotAvailableException;

import java.util.Optional;

public abstract class SearchTypeRequest<T> extends ClientRequest<T> {


    public SearchTypeRequest(String url) {
        super(url);
    }

    protected void save(SearchEntry searchEntry) {
        SharedPreferences.Editor editor = App.getSearchHistoryPreference().edit();
        editor.putString(searchEntry.getKey(), searchEntry.toString());
        editor.apply();
    }

    protected abstract Optional<SearchEntry> extractSearchEntry(T response) throws BookNotAvailableException;
}


