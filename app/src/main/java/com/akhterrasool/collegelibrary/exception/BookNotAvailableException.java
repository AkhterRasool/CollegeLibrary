package com.akhterrasool.collegelibrary.exception;

import com.akhterrasool.collegelibrary.R;

import static com.akhterrasool.collegelibrary.util.AppUtils.getResourceString;

public class BookNotAvailableException extends Throwable {


    public BookNotAvailableException(String title) {
        super(getResourceString(R.string.book_not_available, title));
    }
}
