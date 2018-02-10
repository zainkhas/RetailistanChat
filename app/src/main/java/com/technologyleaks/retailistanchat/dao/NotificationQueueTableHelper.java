package com.technologyleaks.retailistanchat.dao;

import com.technologyleaks.retailistanchat.beans.NotificationQueue;

import java.util.List;

/**
 * Created by zainulabideen on 09/02/2018.
 */

public class NotificationQueueTableHelper {

    private AppDatabase appDatabase;

    public NotificationQueueTableHelper(AppDatabase appDatabase) {
        this.appDatabase = appDatabase;
    }

    public void addNotificationQueue(NotificationQueue notificationQueue) {
        NotificationQueue existing = getNotificationOfThisUser(notificationQueue.getUsername());
        if (existing != null) {
            notificationQueue.setId(existing.getId());
        }

        appDatabase.notificationQueueDao().insertAll(notificationQueue);


    }

    private NotificationQueue getNotificationOfThisUser(String username) {
        return appDatabase.notificationQueueDao().getNotificationOfThisUser(username);
    }


    public List<NotificationQueue> getAll() {
        return appDatabase.notificationQueueDao().getAll();
    }


    public void delete(NotificationQueue notificationQueue) {
        appDatabase.notificationQueueDao().delete(notificationQueue);
    }

}
