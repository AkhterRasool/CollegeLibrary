package com.akhterrasool.collegelibrary.clientrequest;

import android.content.SharedPreferences;

import com.akhterrasool.collegelibrary.app.App;
import com.akhterrasool.collegelibrary.app.model.SearchHistoryEntry;
import com.akhterrasool.collegelibrary.exception.BookNotAvailableException;

import java.util.Optional;

public abstract class SaveableSearchRequest<T> extends ClientRequest<T> {


    public SaveableSearchRequest(String url) {
        super(url);
    }

    protected void save(SearchHistoryEntry searchEntry) {
        SharedPreferences.Editor editor = App.getSearchHistoryPreference().edit();
        editor.putString(searchEntry.getKey(), searchEntry.toString());
        editor.apply();
    }

    protected abstract Optional<SearchHistoryEntry> extractSearchHistoryEntry(T response) throws BookNotAvailableException;
}


