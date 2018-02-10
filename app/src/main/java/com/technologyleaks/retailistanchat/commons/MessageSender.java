package com.technologyleaks.retailistanchat.commons;

import android.content.Context;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.technologyleaks.retailistanchat.beans.Message;
import com.technologyleaks.retailistanchat.beans.NotificationQueue;
import com.technologyleaks.retailistanchat.dao.NotificationQueueTableHelper;

import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by zainulabideen on 10/02/2018.
 */

public class MessageSender {

    public static void send(Context context, String message, NotificationQueueTableHelper notificationQueueTableHelper) {
        DatabaseReference mChatIndicesRef = FirebaseDatabase.getInstance()
                .getReference(Message.TABLENAME);
        String messageId = mChatIndicesRef.push().getKey();

        Message messageObj = new Message();
        messageObj.setText(message);
        messageObj.setUserId(SharedPrefs.getUserId());
        messageObj.setTime(String.valueOf(System.currentTimeMillis()));
        messageObj.setSenderName(SharedPrefs.getUserName());

        mChatIndicesRef.child(messageId).setValue(messageObj, (databaseError, databaseReference) -> {
            if (databaseError == null) {
                //success

                if (CustomMethods.isNetworkConnected(context)) {

                    Disposable disposable = Flowable.just(notificationQueueTableHelper)
                            .subscribeOn(Schedulers.io())
                            .subscribe(table -> {
                                NotificationQueue queue = new NotificationQueue();
                                queue.setUsername(SharedPrefs.getUserName());
                                queue.setMessage(message);

                                table.addNotificationQueue(queue);

                                PushNotificationHelper.sendAll(table.getAll(), (notificationQueue -> {
                                    Flowable.just(notificationQueue)
                                            .subscribeOn(Schedulers.io())
                                            .subscribe(queue1 -> {
                                                table.delete(queue);
                                            });
                                }));
                            });

                }

            }
        });
    }

}
