package com.technologyleaks.retailistanchat.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.technologyleaks.retailistanchat.beans.NotificationQueue;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

/**
 * Created by zainulabideen on 09/02/2018.
 */


@Dao
public interface NotificationQueueDao {

    @Query("SELECT * FROM notification_queue")
    List<NotificationQueue> getAll();

    //    @Query("SELECT * FROM user WHERE uid IN (:userIds)")
//    List<User> loadAllByIds(int[] userIds);
//
    @Query("SELECT * FROM notification_queue WHERE username = :username")
    NotificationQueue getNotificationOfThisUser(String username);

    @Insert(onConflict = REPLACE)
    void insertAll(NotificationQueue... notificationQueues);

    @Delete
    void delete(NotificationQueue notificationQueue);

    @Update
    void updateNotificationQueue(NotificationQueue... notificationQueues);
}
