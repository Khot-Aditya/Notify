package com.ad.app.notify.model;

import java.io.Serializable;

public class NotificationModel implements Serializable {

    private int notificationId; //PRIMARY KEY
    private String notificationDate;
    private String notificationTime;
    private String notificationSubText;
    private String notificationCategory;
    private String notificationTags;
    private boolean isNotificationPinned;

    public int getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(int notificationId) {
        this.notificationId = notificationId;
    }

    public String  getNotificationDate() {
        return notificationDate;
    }

    public void setNotificationDate(String  notificationDate) {
        this.notificationDate = notificationDate;
    }

    public String getNotificationTime() {
        return notificationTime;
    }

    public void setNotificationTime(String notificationTime) {
        this.notificationTime = notificationTime;
    }

    public String getNotificationCategory() {
        return notificationCategory;
    }

    public void setNotificationCategory(String notificationCategory) {
        this.notificationCategory = notificationCategory;
    }

    public String getNotificationSubText() {
        return notificationSubText;
    }

    public void setNotificationSubText(String notificationSubText) {
        this.notificationSubText = notificationSubText;
    }

    public String getNotificationTags() {
        return notificationTags;
    }

    public void setNotificationTags(String notificationTags) {
        this.notificationTags = notificationTags;
    }

    public boolean isNotificationPinned() {
        return isNotificationPinned;
    }

    public void setNotificationPinned(boolean notificationPinned) {
        isNotificationPinned = notificationPinned;
    }
}
