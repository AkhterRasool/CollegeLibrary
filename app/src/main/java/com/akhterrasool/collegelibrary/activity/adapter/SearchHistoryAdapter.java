package com.akhterrasool.collegelibrary.activity.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.akhterrasool.collegelibrary.R;
import com.akhterrasool.collegelibrary.activity.ResultActivity;
import com.akhterrasool.collegelibrary.app.App;
import com.akhterrasool.collegelibrary.app.model.SearchEntry;
import com.akhterrasool.collegelibrary.util.IntentBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class SearchHistoryAdapter extends RecyclerView.Adapter<SearchHistoryAdapter.SearchHistoryViewHolder> {

    private final List<SearchEntry> searchHistoryEntries;
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public SearchHistoryAdapter() {
        searchHistoryEntries = fetchHistory();
    }

    private List<SearchEntry> fetchHistory() {
        Map<String, String> entryMap = (Map<String, String>) App
                .getSearchHistoryPreference()
                .getAll();

        List<SearchEntry> searchEntries = new ArrayList<>(entryMap.size());
        for (String jsonStr : entryMap.values()) {
            try {
                SearchEntry searchEntry = objectMapper.readValue(jsonStr, SearchEntry.class);
                searchEntries.add(searchEntry);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return searchEntries;
    }

    @NonNull
    @Override
    public SearchHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.search_history_list_layout, viewGroup, false);
        return new SearchHistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchHistoryViewHolder searchHistoryViewHolder, int i) {
        SearchEntry searchEntry = searchHistoryEntries.get(i);
        searchHistoryViewHolder.set(searchEntry);
    }

    @Override
    public int getItemCount() {
        return searchHistoryEntries.size();
    }

    class SearchHistoryViewHolder extends RecyclerView.ViewHolder {

        private View rootView;
        private TextView titleText;
        private TextView dateTextView;

        public SearchHistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            this.rootView = itemView;
            titleText = rootView.findViewById(R.id.search_history_item_title_text);
            dateTextView = rootView.findViewById(R.id.search_history_item_date_text);
        }

        public void set(SearchEntry searchEntry) {
            titleText.setText(searchEntry.getKey());
            dateTextView.setText(new Date(searchEntry.getTimeStamp()).toLocaleString());
            rootView.setOnClickListener(v ->
                App.getContext().startActivity(
                        new IntentBuilder()
                                .setActivity(ResultActivity.class)
                                .putExtra(ResultActivity.RESULT_ACTIVITY_RESPONSE_BODY, searchEntry.getValue())
                                .putExtra(ResultActivity.RESULT_ACTIVITY_TITLE, searchEntry.getKey())
                                .build())
            );
        }


    }
}