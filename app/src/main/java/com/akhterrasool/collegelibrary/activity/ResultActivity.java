package com.akhterrasool.collegelibrary.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.akhterrasool.collegelibrary.R;

public class ResultActivity extends AppCompatActivity {

    public static final String RESPONSE_TEXT = "RESPONSE";

    private TextView resultText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        resultText = findViewById(R.id.result_text);
        Intent intent = getIntent();
        String responseText = intent.getStringExtra(RESPONSE_TEXT);
        resultText.setText(responseText);
    }
}
