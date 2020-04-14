package com.akhterrasool.collegelibrary.app.model;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class SearchHistoryEntry {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private String key;
    private String value;
    private long timeStamp;

    public SearchHistoryEntry() {}

    public SearchHistoryEntry(String key, String value) {
        this.key = key;
        this.value = value;
        timeStamp = System.currentTimeMillis();
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
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
