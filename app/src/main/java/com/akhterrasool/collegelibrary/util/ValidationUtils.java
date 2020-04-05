package com.akhterrasool.collegelibrary.util;

import com.akhterrasool.collegelibrary.R;

import static com.akhterrasool.collegelibrary.util.AppUtils.getResourceString;

public class ValidationUtils {

    public static void throwIfNullOrEmpty(String input) {
        if (input == null || input.isEmpty()) {
            throw new IllegalArgumentException(getResourceString(R.string.input_cannot_be_empty));
        }
    }
}
