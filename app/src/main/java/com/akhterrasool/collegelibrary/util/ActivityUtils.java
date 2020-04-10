package com.akhterrasool.collegelibrary.util;

import androidx.appcompat.app.AppCompatActivity;

import com.akhterrasool.collegelibrary.activity.ResultActivity;
import com.akhterrasool.collegelibrary.app.App;

public class ActivityUtils {

    public static void startResultActivity(String title, String body) {
        App.getContext().startActivity(
                new IntentBuilder(ResultActivity.class)
                        .putExtra(ResultActivity.RESULT_ACTIVITY_TITLE, title)
                        .putExtra(ResultActivity.RESULT_ACTIVITY_RESPONSE_BODY, body)
                        .build()
        );
    }


    public static void startActivity(Class<? extends AppCompatActivity> activityToLaunch) {
        App.getContext()
                .startActivity(new IntentBuilder(activityToLaunch).build());
    }

}
