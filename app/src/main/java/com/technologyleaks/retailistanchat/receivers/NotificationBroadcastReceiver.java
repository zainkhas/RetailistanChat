package com.technologyleaks.retailistanchat.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.RemoteInput;
import android.util.Log;

import com.technologyleaks.retailistanchat.MyApplication;
import com.technologyleaks.retailistanchat.commons.MessageSender;
import com.technologyleaks.retailistanchat.dao.AppDatabase;
import com.technologyleaks.retailistanchat.dao.NotificationQueueTableHelper;

import static com.technologyleaks.retailistanchat.commons.NotificationUtil.KEY_TEXT_REPLY;
import static com.technologyleaks.retailistanchat.commons.NotificationUtil.REPLY_ACTION;

/**
 * Created by zainulabideen on 10/02/2018.
 */

public class NotificationBroadcastReceiver extends BroadcastReceiver {

    private static final String TAG = NotificationBroadcastReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {

        if (REPLY_ACTION.equals(intent.getAction())) {
            // do whatever you want with the message. Send to the server or add to the db.
            // for this tutorial, we'll just show it in a toast;
            CharSequence message = getReplyMessage(intent);


            Log.d(TAG, "Reply text: " + message);


            if (message != null && message.length() > 0) {
                AppDatabase appDatabase = ((MyApplication) context).getAppDatabase();

                MessageSender.send(
                        context,
                        message.toString(),
                        new NotificationQueueTableHelper(appDatabase)
                );
            }


        }
    }


    private CharSequence getReplyMessage(Intent intent) {
        Bundle remoteInput = RemoteInput.getResultsFromIntent(intent);
        if (remoteInput != null) {
            return remoteInput.getCharSequence(KEY_TEXT_REPLY);
        }
        return null;
    }
}
