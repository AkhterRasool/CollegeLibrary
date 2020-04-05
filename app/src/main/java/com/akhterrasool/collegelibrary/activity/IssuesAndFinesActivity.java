package com.akhterrasool.collegelibrary.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.akhterrasool.collegelibrary.R;
import com.akhterrasool.collegelibrary.clientrequest.BookIssuedRequest;
import com.akhterrasool.collegelibrary.util.Client;
import com.akhterrasool.collegelibrary.util.ValidationUtils;
import com.android.volley.Request;

import static com.akhterrasool.collegelibrary.util.AppUtils.getResourceString;
import static com.akhterrasool.collegelibrary.util.AppUtils.showShort;

public class IssuesAndFinesActivity extends AppCompatActivity {

    private EditText rollNumberField;
    private Button searchButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.issues_and_fine);

        rollNumberField = findViewById(R.id.roll_number_field);
        searchButton = findViewById(R.id.search_fines_button);

        searchButton.setOnClickListener(view -> checkForIssuesAndFines());
    }

    private void checkForIssuesAndFines() {
        String rollNumber = rollNumberField.getText().toString().trim();
        try {
            ValidationUtils.throwIfNullOrEmpty(rollNumber);
            String url = getResourceString(
                    R.string.issues_and_fines_url,
                    getResourceString(R.string.root_url),
                    rollNumber
            );
            Request bookIssuedRequest = new BookIssuedRequest(url).getRequest();
            Client.send(bookIssuedRequest);
        } catch (IllegalArgumentException ex) {
            showShort(ex.getMessage());
        }
    }
}

