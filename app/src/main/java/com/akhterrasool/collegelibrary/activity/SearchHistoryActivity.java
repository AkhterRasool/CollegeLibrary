package com.akhterrasool.collegelibrary.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.akhterrasool.collegelibrary.R;
import com.akhterrasool.collegelibrary.activity.adapter.SearchHistoryAdapter;

import static com.akhterrasool.collegelibrary.app.App.searchHistoryEntriesExist;

public class SearchHistoryActivity extends AppCompatActivity {


    private RecyclerView recyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_history);

        recyclerView = findViewById(R.id.my_recycler_view);

        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new SearchHistoryAdapter());

        if (searchHistoryEntriesExist()) {
            View noDataToDisplayViewLayout = findViewById(R.id.no_data_display_layout);
            noDataToDisplayViewLayout.setVisibility(View.GONE);
        }
    }
}

