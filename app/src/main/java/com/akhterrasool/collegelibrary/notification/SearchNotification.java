package com.akhterrasool.collegelibrary.notification;

import android.app.PendingIntent;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.TaskStackBuilder;

import com.akhterrasool.collegelibrary.R;
import com.akhterrasool.collegelibrary.activity.ResultActivity;
import com.akhterrasool.collegelibrary.app.App;

public class SearchNotification {

    public static final String CHANNEL_ID = "Search Notification Id";

    public static void createNotification(String textTitle, String textContent, int notificationId) {
        NotificationCompat.Builder builder = getBasicBuilder(textTitle, textContent);
        notify(notificationId, builder);
    }

    public static void createNotificationWithResultActivity(String notificationTitle,
                                                            String notificationContent,
                                                            String resultActivityTitle,
                                                            String resultActivityBody,
                                                            String resultActivityToastMessage,
                                                            int notificationId) {
        Intent resultActivityIntent = new Intent(App.getContext(), ResultActivity.class);
        resultActivityIntent.putExtra(ResultActivity.RESULT_ACTIVITY_TITLE, resultActivityTitle);
        resultActivityIntent.putExtra(ResultActivity.RESULT_ACTIVITY_RESPONSE_BODY, resultActivityBody);
        resultActivityIntent.putExtra(ResultActivity.RESULT_ACTIVITY_TOAST_MESSAGE, resultActivityToastMessage);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(App.getContext());
        stackBuilder.addNextIntentWithParentStack(resultActivityIntent);
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder =
                getBasicBuilder(notificationTitle, notificationContent)
                .setContentIntent(pendingIntent);

        notify(notificationId, builder);
    }

    private static void notify(int notificationId, NotificationCompat.Builder builder) {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(App.getContext());
        notificationManager.notify(notificationId, builder.build());
    }

    private static NotificationCompat.Builder getBasicBuilder(String textTitle, String textContent) {
        return new NotificationCompat.Builder(App.getContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle(textTitle)
                .setContentText(textContent)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(textContent))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);
    }


}
