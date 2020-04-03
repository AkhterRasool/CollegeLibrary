package com.akhterrasool.collegelibrary.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.akhterrasool.collegelibrary.R;
import com.akhterrasool.collegelibrary.activity.notification.MainNotificationActivity;
import com.akhterrasool.collegelibrary.app.App;
import com.akhterrasool.collegelibrary.notification.SearchNotification;
import com.akhterrasool.collegelibrary.util.SubscriptionUtils;
import com.akhterrasool.collegelibrary.worker.SearchWorker;

import static com.akhterrasool.collegelibrary.util.AppUtils.getResourceString;
import static com.akhterrasool.collegelibrary.util.AppUtils.showLong;
import static com.akhterrasool.collegelibrary.worker.SearchWorker.BACKGROUND_SEARCH_COMPLETED_NOTIFICATION_ID;

/**
 * Background service which keeps running, even if the app is closed, until stopped explicitly
 * or until a condition fails.
 *
 * The {@link SearchService} will only trigger {@link SearchWorker} to do background search and then
 * wait until there are no items in the subscription set. This class wont perform any background search.
 *
 * If the app is closed while the service is running, the service is restarted with the same {@link Intent}
 * used rather than creating a new {@link Intent}.
 * If the user wishes not to run this service or to stop it, then they must clear the subscription set
 * @see MainNotificationActivity#stopNotificationsButton() functionality.
 *
 * By extending {@link SearchService} from {@link IntentService}, the developer need not handle
 * creation/starting of thread as creation/starting of thread is done by {@link IntentService}.
 *
 * The background task must be defined in {@link SearchService#onHandleIntent(Intent)}
 */
public class SearchService extends IntentService {

    //TAG used for logging purpose i.e, to identify from which class the log message is coming from.
    private static final String TAG = "com.akhterrasool.collegelibrary.service.SearchService";
    //To keep track of the status of this service i.e, if it's running or not.
    private static boolean isRunning;
    //Assign a separate notification when the service completes. The id is used as follows:
    private static final int SERVICE_COMPLETE_STATUS_NOTIFICATION_ID = 28397;

    public SearchService() {
        super("SearchService"); //Pass in the name of the service.
        //Redeliver the intent if the app is closed in the middle of service execution
        //i.e, the service will restart and the same task is executed again prior to restart.
        //In order to achieve this, we setIntentRedeliver as true to redeliver the last intent under execution.
        setIntentRedelivery(true);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        //There's no need to use this if the service is started via app.
        //If the service is started in background or when the device reboots,
        //there is no context as the app is not opened. Hence if service started in background, we set the context.
        App.setContext(getApplicationContext()); //Mandatory
        Log.i(TAG, "onHandleIntent: Triggering the search worker from service.");
        SearchWorker.trigger();

        //Wait until subscription set has at least one item left.
        while (SubscriptionUtils.atLeastOneNotificationItemExists()) {
            //In order to avoid CPU cycles to be wasted, we simply cause this thread to sleep.
            try {
                Thread.sleep(5000);//Some random sleep time is assigned. 5 seconds in this case.
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (App.isAppRunning()) {
            notifyCompletionViaToastMessage();
        } else {
            notifyCompletionViaNotification();
        }
        Log.i(TAG, "onHandleIntent: Service completed.");
        //A service stops itself if this method is executed.
        //If service is caused to redeliver last intent, then it must be explicitly stopped.
        //Hence we use stopSelf().
        stopSelf();
    }

    private void notifyCompletionViaNotification() {
        String title = getResourceString(R.string.search_service_module_name);
        String content = getResourceString(R.string.background_search_not_running);
        SearchNotification.createNotification(title, content, BACKGROUND_SEARCH_COMPLETED_NOTIFICATION_ID);
    }

    private void notifyCompletionViaToastMessage() {
        //This operation is performed on separate thread i.e, not on the main thread.
        //Only main thread can update the UI/show messages.
        //Hence we request the looper of the main thread (which loops through the queue containing tasks)
        //to process the task from this thread.
        //Hence the task is executed as if it's being executed on main thread.
        getMainLooper(); //Inform the main looper that we are going to assign some task to process on main thread.
        //The looper of main thread will take task from the queue of this thread and process.
        showLong(getResourceString(R.string.background_search_not_running)); //Do some task
        Looper.loop();//Now inform the looper to take our task and process it.
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isRunning = false;
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        isRunning = true;
        // We have invoked setIntentRedilvery(true) in constructor.
        // This method will ultimately will return START_REDELIVER_INTENT which will keep make sure
        // that the service is properly executed.
        return super.onStartCommand(intent, flags, startId);
    }

    public static boolean isRunning() {
        return isRunning;
    }
}

