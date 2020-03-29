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

import com.akhterrasool.collegelibrary.R;
import com.akhterrasool.collegelibrary.app.App;
import com.akhterrasool.collegelibrary.app.BookSearchType;
import com.akhterrasool.collegelibrary.clientrequest.AuthorSearchType;
import com.akhterrasool.collegelibrary.clientrequest.TitleSearchType;
import com.akhterrasool.collegelibrary.util.Client;
import com.android.volley.Request;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class SearchActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private EditText bookTextField;
    private Button searchButton;
    private Button clearButton;
    private Spinner spinner;
    private TextView bookTextFieldLabel;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));

        bookTextField = findViewById(R.id.book_text_field);
        searchButton = findViewById(R.id.search_button);
        clearButton = findViewById(R.id.clear_button);
        bookTextFieldLabel = findViewById(R.id.book_text_field_label);
        spinner = findViewById(R.id.search_type_drop_down);

        initializeSearchTypeDropDown();

        searchButton.setOnClickListener(view -> {
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

    private void sendSearchRequest(String book) {
        Log.i(SearchActivity.class.getName(), "User has entered '" + book + "'");
        String selectedSearchType = spinner.getSelectedItem().toString();
        String url =
                String.format("%s/search/%s/%s", getResources().getString(R.string.root_url),
                        selectedSearchType.toLowerCase(), book);
        Log.i(SearchActivity.class.getName(), "Navigating to " + url);

        Request request = null;
        switch (BookSearchType.valueOf(selectedSearchType.toUpperCase())) {
            case TITLE:
                request = new TitleSearchType(url).getRequest();
                break;
            case AUTHOR:
                request = new AuthorSearchType(url).getRequest();
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
