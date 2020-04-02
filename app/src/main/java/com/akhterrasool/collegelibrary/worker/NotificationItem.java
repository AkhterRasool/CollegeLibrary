package com.akhterrasool.collegelibrary.worker;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * This class represents the subscription which the user has registered for background search.
 * Mainly used for:
 *  1. Checking if the subscription item is no longer useful as the search operation has been completed or failed.
 *  2. Maintaining a separate notification for each of the subscription item.
 *
 *  For instance, "Embedded System" and "Operating systems concepts". Each of these is considered a
 *  subscription item and a separate notification item is assigned for each of these to update
 *  the search status whether the search has completed or failed or the user has passed an invalid input.
 * @see SearchWorker to understand how this class is used for achieving functionality.
 */
public class NotificationItem {
    private static final int NOTIFICATION_ITEM_INITIAL_NOTIFICATION_ID = 20;
    private static AtomicInteger notificationIdGenerator = new AtomicInteger(NOTIFICATION_ITEM_INITIAL_NOTIFICATION_ID);
    private int notificationId;
    private String input;
    private boolean noLongerUseful;

    public NotificationItem(String input) {
        this.input = input;
        this.notificationId = notificationIdGenerator.getAndIncrement();
        noLongerUseful = false;
    }

    public int getNotificationId() {
        return notificationId;
    }

    public String getInput() {
        return input;
    }

    public void setNoLongerUseful(boolean noLongerUseful) {
        this.noLongerUseful = noLongerUseful;
    }

    public boolean isNoLongerUseful() {
        return noLongerUseful;
    }
}
