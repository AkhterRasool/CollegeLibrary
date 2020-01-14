package com.akhterrasool.collegelibrary.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.akhterrasool.collegelibrary.R;
import com.akhterrasool.collegelibrary.app.App;

public class HomeActivity extends AppCompatActivity {

    private Button searchActivityButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        App.setContext(getApplicationContext());

        searchActivityButton = findViewById(R.id.search_activity_button);

        searchActivityButton.setOnClickListener(view -> {
            Intent searchActivityIntent = new Intent(getApplicationContext(), SearchActivity.class);
            startActivity(searchActivityIntent);
        });
    }
}
