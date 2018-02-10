package com.technologyleaks.retailistanchat.commons;

import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.google.gson.Gson;
import com.technologyleaks.retailistanchat.beans.FCMResponse;
import com.technologyleaks.retailistanchat.beans.NotificationQueue;

import java.util.List;

/**
 * Created by zainulabideen on 09/02/2018.
 */

public class PushNotificationHelper {

    private static final String TAG = PushNotificationHelper.class.getSimpleName();


    public static void sendAll(List<NotificationQueue> mQueue, Listener listener) {
        if (mQueue == null) {
            return;
        }

        Log.d(TAG, "Sending " + mQueue.size() + " notifications...");

        send(mQueue, 0, listener);

    }


    private static void send(List<NotificationQueue> mQueue, int index, Listener listener) {

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

                        try {
                            FCMResponse fcmResponse = new Gson().fromJson(response, FCMResponse.class);
                            if (fcmResponse != null && fcmResponse.getMessage_id().length() > 0) {
                                listener.onSuccess(notificationQueue);
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }

                        moveToNext(mQueue, index, listener);
                    }

                    @Override
                    public void onError(ANError anError) {
                        anError.printStackTrace();
                        moveToNext(mQueue, index, listener);
                    }
                });
    }


    private static void moveToNext(List<NotificationQueue> mQueue, int index, Listener listener) {
        if ((index + 1) < mQueue.size()) {
            send(mQueue, index + 1, listener);
        }
    }


    public interface Listener {
        void onSuccess(NotificationQueue notificationQueue);
    }

}
