package com.akhterrasool.collegelibrary.worker;

import java.util.concurrent.atomic.AtomicInteger;

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
