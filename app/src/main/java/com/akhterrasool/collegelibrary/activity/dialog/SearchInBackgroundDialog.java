package com.akhterrasool.collegelibrary.activity.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.akhterrasool.collegelibrary.R;

import static com.akhterrasool.collegelibrary.util.AppUtils.getResourceString;
import static com.akhterrasool.collegelibrary.util.AppUtils.showShort;
import static com.akhterrasool.collegelibrary.util.AppUtils.startSearchService;
import static com.akhterrasool.collegelibrary.util.SubscriptionUtils.addItemToSubscription;

public class SearchInBackgroundDialog extends DialogFragment {

    private final String input;

    public SearchInBackgroundDialog(String input) {
        this.input = input;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        dialogBuilder.setTitle(getResourceString(R.string.book_not_available))
                .setMessage(getResourceString(R.string.ask_to_search_book_in_background))
                .setPositiveButton(R.string.sure, (dialog, which) -> registerBookForNotifications())
                .setNegativeButton(R.string.no_thanks, (dialog, which) -> {/* Nothing */})
                .setCancelable(true);

        return dialogBuilder.create();

    }

    private void registerBookForNotifications() {
        try {
            addItemToSubscription(input);
            startSearchService();
            showShort(getResourceString(R.string.notification_item_registration_success, input));
        } catch (IllegalArgumentException ex) {
            showShort(ex.getMessage());
        }
    }
}
