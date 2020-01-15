package com.akhterrasool.collegelibrary.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.akhterrasool.collegelibrary.R;

public class ResultActivity extends AppCompatActivity {

    public static final String RESULT_ACTIVITY_RESPONSE_BODY = "RESPONSE";
    public static final String RESULT_ACTIVITY_TITLE = "TITLE";

    private TextView resultText;
    private TextView resultTitle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        resultText = findViewById(R.id.result_text);
        resultTitle = findViewById(R.id.result_activity_title);

        Intent intent = getIntent();
        String responseText = intent.getStringExtra(RESULT_ACTIVITY_RESPONSE_BODY);
        String responseTitle = intent.getStringExtra(RESULT_ACTIVITY_TITLE);
        resultText.setText(responseText);
        resultTitle.setText(responseTitle);
    }
}
