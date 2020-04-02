package com.akhterrasool.collegelibrary.activity.notification;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.akhterrasool.collegelibrary.R;
import com.akhterrasool.collegelibrary.activity.SearchActivity;

import static com.akhterrasool.collegelibrary.util.AppUtils.getResourceString;
import static com.akhterrasool.collegelibrary.util.AppUtils.showLong;
import static com.akhterrasool.collegelibrary.util.AppUtils.startSearchService;
import static com.akhterrasool.collegelibrary.util.SubscriptionUtils.addItemToSubscription;

public class NotificationRegistrationActivity extends SearchActivity {

    private TextView titleText;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        titleText = findViewById(R.id.title_text);
        titleText.setText(R.string.notification_registration_activity_title_text);
        primaryButton.setText(getResourceString(R.string.subscribe_text));
        spinner.setVisibility(View.GONE);
        primaryButton.setOnClickListener(view -> registerBookForNotifications());
    }

    private void registerBookForNotifications() {
        String input = bookTextField.getText().toString().trim();
        try {
            validate(input);
            addItemToSubscription(input);
            startSearchService();
            showLong(getResourceString(R.string.notification_item_registration_success, input));
        } catch (IllegalArgumentException ex) {
            showLong(ex.getMessage());
        }
    }

    private void validate(String input) {
        if (input == null || input.isEmpty()) {
            throw new IllegalArgumentException(getResourceString(R.string.input_cannot_be_empty));
        }
    }
}


