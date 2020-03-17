package com.akhterrasool.collegelibrary.app.model;

import com.akhterrasool.collegelibrary.app.BookSearchType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class SearchEntry {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private String key;
    private String value;
    private BookSearchType bookSearchType;
    private long timeStamp;

    public SearchEntry() {}

    public SearchEntry(String key, String value, BookSearchType bookSearchType) {
        this.key = key;
        this.value = value;
        this.bookSearchType = bookSearchType;
        timeStamp = System.currentTimeMillis();
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public BookSearchType getBookSearchType() {
        return bookSearchType;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    @Override
    public String toString() {
        String objString = "";
        try {
            objString = objectMapper.writeValueAsString(this);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return objString;
    }
}
