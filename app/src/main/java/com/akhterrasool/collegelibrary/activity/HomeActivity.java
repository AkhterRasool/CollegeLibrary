package com.akhterrasool.collegelibrary.activity;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.akhterrasool.collegelibrary.R;
import com.akhterrasool.collegelibrary.app.App;
import com.akhterrasool.collegelibrary.util.ActivityUtils;

import static com.akhterrasool.collegelibrary.util.AppUtils.showShort;

public class HomeActivity extends AppCompatActivity {

    private Button searchActivityButton;
    private Button searchHistoryActivityButton;
    private Button clearSearchHistoryButton;
    private Button issuesAndFinesActivityButton;
    private Button notificationsModuleButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        App.init(getApplicationContext());
        App.setAppRunning(true);

        searchActivityButton = findViewById(R.id.search_activity_button);
        searchHistoryActivityButton = findViewById(R.id.search_history_activity_button);
        clearSearchHistoryButton = findViewById(R.id.clear_search_history_button);
        issuesAndFinesActivityButton = findViewById(R.id.issues_and_fines_button);
        notificationsModuleButton = findViewById(R.id.notifications_module_button);

        searchActivityButton.setOnClickListener(view ->
                ActivityUtils.startActivity(SearchActivity.class)
        );

        searchHistoryActivityButton.setOnClickListener(view ->
                ActivityUtils.startActivity(SearchHistoryActivity.class)
        );

        issuesAndFinesActivityButton.setOnClickListener(view ->
                ActivityUtils.startActivity(IssuesAndFinesActivity.class)
        );

        notificationsModuleButton.setOnClickListener(view ->
                ActivityUtils.startActivity(MainNotificationActivity.class)
        );

        clearSearchHistoryButton.setOnClickListener(view -> clearSearchHistory());
    }

    public void clearSearchHistory() {
        if (App.searchHistoryEntriesExist()) {
            boolean clearSuccessful = App.getSearchHistoryPreference().edit().clear().commit();
            if (clearSuccessful) {
                showShort("Search history was cleared successfully.");
            } else {
                showShort("Failed to clear search history.");
            }
        } else {
            showShort("There is no history to clear!");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        App.setAppRunning(false);
    }
}
