package com.akhterrasool.collegelibrary.util;


import android.widget.Toast;

import com.akhterrasool.collegelibrary.app.App;

public class ToastMessage {

    public static void showLong(String message) {
        Toast.makeText(App.getContext(), message, Toast.LENGTH_LONG).show();
    }
}
