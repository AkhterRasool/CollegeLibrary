package com.akhterrasool.collegelibrary.util;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import com.akhterrasool.collegelibrary.app.App;

import java.util.HashMap;
import java.util.Map;

public class IntentBuilder {

    private Class<? extends AppCompatActivity> activity;
    private Map<String, String> extrasMap;

    public IntentBuilder(Class<? extends AppCompatActivity> activityToLaunch) {
        extrasMap = new HashMap<>();
        this.activity = activityToLaunch;
    }


    public IntentBuilder putExtra(String key, String value) {
        this.extrasMap.put(key, value);
        return this;
    }

    public Intent build() {
        Intent intent = new Intent(App.getContext(), activity);
        extrasMap.forEach((key, value) -> intent.putExtra(key, value));
        return intent;
    }
}
