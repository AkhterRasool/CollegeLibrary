package com.akhterrasool.collegelibrary.activity;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.akhterrasool.collegelibrary.R;
import com.akhterrasool.collegelibrary.activity.adapter.SearchHistoryAdapter;

public class SearchHistoryActivity extends AppCompatActivity {


    private RecyclerView recyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_history);
        View noDataToDisplayViewLayout = findViewById(R.id.no_data_display_layout);
        noDataToDisplayViewLayout.setVisibility(View.GONE);
        recyclerView = findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new SearchHistoryAdapter());
    }
}

