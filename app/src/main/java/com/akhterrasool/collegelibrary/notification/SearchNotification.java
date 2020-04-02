package com.akhterrasool.collegelibrary.notification;

import android.app.PendingIntent;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.TaskStackBuilder;

import com.akhterrasool.collegelibrary.R;
import com.akhterrasool.collegelibrary.activity.ResultActivity;
import com.akhterrasool.collegelibrary.app.App;
import com.akhterrasool.collegelibrary.service.SearchService;
import com.akhterrasool.collegelibrary.worker.SearchWorker;

/**
 * This class represents a notification used only for {@link SearchService} and {@link SearchWorker}.
 * It's not a good idea to use this class to send notifications for functionalities of this App.
 * Used only to provide notifications regarding search status, results, service completion status, etc.
 */
public class SearchNotification {

    /**
     * Please
     * @see App#createSearchNotificationChannel()
     */
    public static final String CHANNEL_ID = "Search Notification Id";

    /**
     * Create a notification which does nothing when the user taps on it.
     * @param textTitle
     * @param textContent
     * @param notificationId
     */
    public static void createNotification(String textTitle, String textContent, int notificationId) {
        //We first specify how the notification should be build with some title and content.
        NotificationCompat.Builder builder = getBasicBuilder(textTitle, textContent);
        //Build the notification with some id. Each notification has an id. If the same id is used,
        //the notification with that id is updated rather than creating a new one.
        //After building notify the user.
        notify(notificationId, builder);
    }

    /**
     * Create a notification which launches the {@link ResultActivity} when the user taps on it.
     * @param notificationTitle
     * @param notificationContent
     * @param resultActivityTitle
     * @param resultActivityBody
     * @param resultActivityToastMessage
     * @param notificationId
     */
    public static void createNotificationWithResultActivity(String notificationTitle,
                                                            String notificationContent,
                                                            String resultActivityTitle,
                                                            String resultActivityBody,
                                                            String resultActivityToastMessage,
                                                            int notificationId) {
        //Create the intent for ResultActivity.
        Intent resultActivityIntent = new Intent(App.getContext(), ResultActivity.class);
        //Pass in the parameters/extras to the result activity for it to display.
        //These parameters specify what the ResultActivity should display.
        resultActivityIntent.putExtra(ResultActivity.RESULT_ACTIVITY_TITLE, resultActivityTitle);
        resultActivityIntent.putExtra(ResultActivity.RESULT_ACTIVITY_RESPONSE_BODY, resultActivityBody);
        resultActivityIntent.putExtra(ResultActivity.RESULT_ACTIVITY_TOAST_MESSAGE, resultActivityToastMessage);

        //When the ResultActivity is launched, the user should be able to navigate back to the parent activity
        //upon clicking the back button. Hence we create a stack builder which maintains the parent
        //activity to direct to when the back button is clicked.
        //Parent activity is assigned in AndroidManifest.xml
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(App.getContext());
        stackBuilder.addNextIntentWithParentStack(resultActivityIntent); //Take this intent and assign the parent activity to be previous activity.
        //From the stack builder get the intent which is pending and ready to launch.
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(
                0,
                PendingIntent.FLAG_UPDATE_CURRENT); //If the intent is being displayed simply update the intent
        //i.e, if the ResultActivity is being displayed simply update it otherwise create a new one.

        NotificationCompat.Builder builder =
                getBasicBuilder(notificationTitle, notificationContent)
                .setContentIntent(pendingIntent);

        notify(notificationId, builder);
    }

    private static void notify(int notificationId, NotificationCompat.Builder builder) {
        //The manager notifies the user.
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(App.getContext());//So we initialise it
        notificationManager.notify(notificationId, builder.build()); //notify the user with the notification's id and how it's supposed to be build.
    }

    private static NotificationCompat.Builder getBasicBuilder(String textTitle, String textContent) {
        //Initialize the builder with the context and the channel id.
        return new NotificationCompat.Builder(App.getContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_background) //Assign some icon
                .setContentTitle(textTitle) //Some title
                .setContentText(textContent)//and some text
                //If the text requires more than one line to be displayed and style the text as BigTextStyle
                .setStyle(new NotificationCompat.BigTextStyle().bigText(textContent))  //and pass in the content.
                //Priority represents how the user should be notified i.e, with sound, vibration, or interrupt the user from top and slide back.
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true); //After the user taps in, cancel it. Remove this line if you want the notification to stay when the user taps in.
    }


}
