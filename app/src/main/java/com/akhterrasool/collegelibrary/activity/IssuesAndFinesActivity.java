package com.akhterrasool.collegelibrary.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;

import com.akhterrasool.collegelibrary.R;
import com.akhterrasool.collegelibrary.clientrequest.BookIssuedRequest;
import com.akhterrasool.collegelibrary.util.Client;
import com.android.volley.Request;

import static com.akhterrasool.collegelibrary.util.AppUtils.showLong;

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
            validate(rollNumber);
            String url = getResources().getString(R.string.root_url) + "/search/issuesandfines/" + rollNumber;
            Request bookIssuedRequest = new BookIssuedRequest(url).getRequest();
            Client.send(bookIssuedRequest);
        } catch (IllegalArgumentException ex) {
            showLong(ex.getMessage());
        }
    }

    private void validate(String rollNumber) {
        if (rollNumber == null || rollNumber.isEmpty()) {
            throw new IllegalArgumentException("Roll number cannot be empty.");
        }
    }
}
