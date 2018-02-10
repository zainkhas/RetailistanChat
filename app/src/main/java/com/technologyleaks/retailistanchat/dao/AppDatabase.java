package com.technologyleaks.retailistanchat.dao;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.technologyleaks.retailistanchat.beans.NotificationQueue;

@Database(entities = {NotificationQueue.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract NotificationQueueDao notificationQueueDao();
}