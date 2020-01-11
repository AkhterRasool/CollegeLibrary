package com.akhterrasool.collegelibrary;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class HomeActivity extends AppCompatActivity {

    private Button searchActivityButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        searchActivityButton = findViewById(R.id.search_activity_button);

        searchActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent searchActivityIntent = new Intent(getApplicationContext(), SearchActivity.class);
                startActivity(searchActivityIntent);
            }
        });
    }
}
