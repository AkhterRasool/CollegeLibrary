package com.akhterrasool.collegelibrary.util;

import android.content.Intent;

import com.akhterrasool.collegelibrary.app.App;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class IntentBuilder {

    private Class activity;
    private Map<String, String> extrasMap;

    public IntentBuilder() {
        extrasMap = new HashMap<>();
    }

    public IntentBuilder setActivity(Class activityToLaunch) {
        this.activity = activityToLaunch;
        return this;
    }


    public IntentBuilder putExtra(String key, String value) {
        this.extrasMap.put(key, value);
        return this;
    }

    public Intent build() {
        Objects.requireNonNull(activity, "Activity cannot be null before building an intent");
        Intent intent = new Intent(App.getContext(), activity);
        extrasMap.forEach((key, value) -> intent.putExtra(key, value));
        return intent;
    }
}
