package com.akhterrasool.collegelibrary.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.akhterrasool.collegelibrary.R;
import com.akhterrasool.collegelibrary.app.App;
import com.akhterrasool.collegelibrary.util.IntentBuilder;

import static com.akhterrasool.collegelibrary.util.AppUtils.showLong;

public class HomeActivity extends AppCompatActivity {

    private Button searchActivityButton;
    private Button searchHistoryActivityButton;
    private Button clearSearchHistoryButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        App.setContext(getApplicationContext());

        searchActivityButton = findViewById(R.id.search_activity_button);
        searchHistoryActivityButton = findViewById(R.id.search_history_activity_button);
        clearSearchHistoryButton = findViewById(R.id.clear_search_history_button);

        searchActivityButton.setOnClickListener(view ->
            startActivity(
                    new IntentBuilder()
                    .setActivity(SearchActivity.class)
                    .build()
            )
        );

        searchHistoryActivityButton.setOnClickListener(view ->
            startActivity(
                    new IntentBuilder()
                    .setActivity(SearchHistoryActivity.class)
                    .build()
            )
        );

        clearSearchHistoryButton.setOnClickListener(view -> clearSearchHistory());
    }

    public void clearSearchHistory() {
        if (App.searchHistoryEntriesExist()) {
            boolean clearSuccessful = App.getSearchHistoryPreference().edit().clear().commit();
            if (clearSuccessful) {
                showLong("Search history was cleared successfully.");
            } else {
                showLong("Failed to clear search history.");
            }
        } else {
            showLong("There is no history to clear!");
        }
    }
}
