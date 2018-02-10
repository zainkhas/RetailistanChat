package com.technologyleaks.retailistanchat.commons;

import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.technologyleaks.retailistanchat.beans.NotificationQueue;

import java.util.List;

/**
 * Created by zainulabideen on 09/02/2018.
 */

public class PushNotificationHelper {

    private static final String TAG = PushNotificationHelper.class.getSimpleName();


    public static void sendAll(List<NotificationQueue> mQueue) {
        if (mQueue == null) {
            return;
        }

        Log.d(TAG, "Sending " + mQueue.size() + " notifications...");

        send(mQueue, 0);

    }


    private static void send(List<NotificationQueue> mQueue, int index) {

        Log.d(TAG, "Current index: " + index);

        NotificationQueue notificationQueue = mQueue.get(index);


        Log.d(TAG, "URL: " + NetworkConstants.URL_FCM);

        AndroidNetworking.post(NetworkConstants.URL_FCM)
                .addBodyParameter(NetworkConstants.PARAM_MESSAGE, notificationQueue.getMessage())
                .addBodyParameter(NetworkConstants.PARAM_TITLE, notificationQueue.getUsername())
                .setPriority(Priority.HIGH)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "FCM response: " + response);

                        moveToNext(mQueue, index);
                    }

                    @Override
                    public void onError(ANError anError) {
                        anError.printStackTrace();
                        moveToNext(mQueue, index);
                    }
                });
    }


    private static void moveToNext(List<NotificationQueue> mQueue, int index) {
        if ((index + 1) < mQueue.size()) {
            send(mQueue, index + 1);
        }
    }

}
