package com.akhterrasool.collegelibrary;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SearchActivity extends AppCompatActivity {

    private EditText bookTextField;
    private Button searchButton;
    private Button clearButton;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));

        bookTextField = findViewById(R.id.book_text_field);
        searchButton = findViewById(R.id.search_button);
        clearButton = findViewById(R.id.clear_button);


        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String book = bookTextField.getText().toString();
                if (!book.isEmpty()) {
                    Log.i(SearchActivity.class.getName(), "User has entered '" + book + "'");
                }
            }
        });

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookTextField.setText(R.string.empty_string);
                Log.i(SearchActivity.class.getName(), "Book Text field has been cleared.");
            }
        });
    }
}
