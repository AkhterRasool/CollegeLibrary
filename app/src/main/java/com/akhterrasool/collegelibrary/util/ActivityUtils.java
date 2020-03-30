package com.akhterrasool.collegelibrary.util;

import com.akhterrasool.collegelibrary.activity.ResultActivity;
import com.akhterrasool.collegelibrary.app.App;

import androidx.appcompat.app.AppCompatActivity;

public class ActivityUtils {

    public static void startResultActivity(String title, String body) {
        App.getContext().startActivity(
                new IntentBuilder()
                        .setActivity(ResultActivity.class)
                        .putExtra(ResultActivity.RESULT_ACTIVITY_TITLE, title)
                        .putExtra(ResultActivity.RESULT_ACTIVITY_RESPONSE_BODY, body)
                        .build()
        );
    }


    public static void startActivity(Class<? extends AppCompatActivity> activityToLaunch) {
        App.getContext()
                .startActivity(new IntentBuilder().setActivity(activityToLaunch).build());
    }

}
