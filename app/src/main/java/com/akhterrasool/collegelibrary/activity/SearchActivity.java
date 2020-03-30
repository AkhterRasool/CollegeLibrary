package com.akhterrasool.collegelibrary.activity;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.akhterrasool.collegelibrary.R;
import com.akhterrasool.collegelibrary.app.App;
import com.akhterrasool.collegelibrary.app.BookSearchType;
import com.akhterrasool.collegelibrary.clientrequest.DisplayAuthorResults;
import com.akhterrasool.collegelibrary.clientrequest.DisplayTitleResults;
import com.akhterrasool.collegelibrary.util.Client;
import com.android.volley.Request;

public class SearchActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    protected EditText bookTextField;
    protected Button primaryButton;
    protected Spinner spinner;
    private Button clearButton;
    private TextView bookTextFieldLabel;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));

        bookTextField = findViewById(R.id.book_text_field);
        primaryButton = findViewById(R.id.primary_button);
        clearButton = findViewById(R.id.clear_button);
        bookTextFieldLabel = findViewById(R.id.book_text_field_label);
        spinner = findViewById(R.id.search_type_drop_down);

        initializeSearchTypeDropDown();

        primaryButton.setOnClickListener(view -> {
            String book = bookTextField.getText().toString();
            if (!book.isEmpty()) {
                sendSearchRequest(book);
            }
        });

        clearButton.setOnClickListener(view -> {
            bookTextField.setText(R.string.empty_string);
            Log.i(SearchActivity.class.getName(), "Book Text field has been cleared.");
        });
    }

    private void initializeSearchTypeDropDown() {
        ArrayAdapter<CharSequence> spinnerMenuItemSource = ArrayAdapter.createFromResource(
            App.getContext(), R.array.search_type_array, android.R.layout.simple_spinner_item);
        spinnerMenuItemSource.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerMenuItemSource);
        spinner.setOnItemSelectedListener(this);
    }

    private void sendSearchRequest(String input) {
        Log.i(SearchActivity.class.getName(), "User has entered '" + input + "'");
        String selectedSearchType = spinner.getSelectedItem().toString();

        Request request = null;
        switch (BookSearchType.valueOf(selectedSearchType.toUpperCase())) {
            case TITLE:
                request = new DisplayTitleResults(input).getRequest();
                break;
            case AUTHOR:
                request = new DisplayAuthorResults(input).getRequest();
                break;
        }

        Client.send(request);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String itemSelected = parent.getItemAtPosition(position).toString();
        switch (BookSearchType.valueOf(itemSelected.toUpperCase())) {
            case TITLE:
                bookTextFieldLabel.setText(R.string.book_text_field_as_title);
                break;
            case AUTHOR:
                bookTextFieldLabel.setText(R.string.book_text_field_as_author);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {}
}
