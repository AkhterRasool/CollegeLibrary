package com.akhterrasool.collegelibrary.activity.notification;

import android.os.Bundle;
import android.view.View;

import com.akhterrasool.collegelibrary.R;
import com.akhterrasool.collegelibrary.activity.SearchActivity;
import com.akhterrasool.collegelibrary.util.AppUtils;

import androidx.annotation.Nullable;

import static com.akhterrasool.collegelibrary.util.AppUtils.getResourceString;
import static com.akhterrasool.collegelibrary.util.AppUtils.showLong;
import static com.akhterrasool.collegelibrary.util.AppUtils.startNotificationService;
import static com.akhterrasool.collegelibrary.util.NotificationUtils.addItemToSubscription;

public class NotificationRegistrationActivity extends SearchActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        primaryButton.setText(getResourceString(R.string.subscribe_text));
        spinner.setVisibility(View.GONE);
        primaryButton.setOnClickListener(view -> registerBookForNotifications());
    }

    private void registerBookForNotifications() {
        String input = bookTextField.getText().toString().trim();
        try {
            validate(input);
            addItemToSubscription(input);
            startNotificationService();
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


