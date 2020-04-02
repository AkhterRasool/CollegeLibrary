package com.akhterrasool.collegelibrary.util;

import androidx.appcompat.app.AppCompatActivity;

import com.akhterrasool.collegelibrary.activity.ResultActivity;
import com.akhterrasool.collegelibrary.app.App;

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

    /**
     * Just a utility method to start the activity
     * @param activityToLaunch which only accepts child classes of {@link AppCompatActivity} since
     *                         for a class to be an activity class it must extend {@link AppCompatActivity}.
     *                         Any random class cannot be passed since the sole purpose of this method
     *                         is start an activity. Also using Java generics in such cases prevents
     *                         conditional checks and errors for the activityToLaunch parameter.
     */
    public static void startActivity(Class<? extends AppCompatActivity> activityToLaunch) {
        App.getContext()
                .startActivity(new IntentBuilder().setActivity(activityToLaunch).build());
    }

}
