package com.akhterrasool.collegelibrary.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.akhterrasool.collegelibrary.R;

import static com.akhterrasool.collegelibrary.util.AppUtils.showLong;

public class ResultActivity extends AppCompatActivity {

    public static final String RESULT_ACTIVITY_RESPONSE_BODY = "RESPONSE";
    public static final String RESULT_ACTIVITY_TITLE = "TITLE";
    public static final String RESULT_ACTIVITY_TOAST_MESSAGE = "TOAST";

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

        //Some extra information to be displayed but not to be kept around.
        //This should not be as part of response.
        //Good Example: Results Saved.
        //Bad Example: Locations available at Rack - 1 Col - 2 Row - 10
        String toastMessage = intent.getStringExtra(RESULT_ACTIVITY_TOAST_MESSAGE);
        resultText.setText(responseText);
        resultTitle.setText(responseTitle);

        if (toastMessage != null) {
            showLong(toastMessage);
        }
    }
}
