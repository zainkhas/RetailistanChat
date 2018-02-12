package com.technologyleaks.retailistanchat.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.RemoteInput;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.technologyleaks.retailistanchat.R;
import com.technologyleaks.retailistanchat.main.view.MainActivity;
import com.technologyleaks.retailistanchat.receivers.NotificationBroadcastReceiver;

import static com.technologyleaks.retailistanchat.commons.NotificationUtil.KEY_NOTIFICATION_ID;
import static com.technologyleaks.retailistanchat.commons.NotificationUtil.KEY_TEXT_REPLY;
import static com.technologyleaks.retailistanchat.commons.NotificationUtil.REPLY_ACTION;
import static com.technologyleaks.retailistanchat.commons.NotificationUtil.mNotificationId;

/**
 * Created by Shahzore on 09-Feb-18.
 */

public class FirebaseMessageService extends FirebaseMessagingService {
    public static String TAG = FirebaseMessageService.class.getSimpleName();


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // ...

        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
        }

//        // Check if message contains a notification payload.
//        if (remoteMessage.getNotification() != null) {
//            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
//        }


        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // Builds the notification and issues it.


        //Inline Reply
        String replyLabel = getResources().getString(R.string.reply_label);
        RemoteInput remoteInput = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT_WATCH) {
            remoteInput = new RemoteInput.Builder(KEY_TEXT_REPLY)
                    .setLabel(replyLabel)
                    .build();
        }


        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this, "group_messages_channel")
                        .setSmallIcon(R.drawable.ic_message_white_24dp_1x)
                        .setContentTitle(remoteMessage.getData().get("title"))
                        .setContentText(remoteMessage.getData().get("body"))
                        .setShowWhen(true);


        PendingIntent pendingIntent = null;
        Intent intent;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

            intent = new Intent(this, NotificationBroadcastReceiver.class);
            intent.setAction(REPLY_ACTION);
            intent.putExtra(KEY_NOTIFICATION_ID, mNotificationId);

            pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 100, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
        } else {
            // start your activity for Android M and below
            intent = new Intent(this, MainActivity.class);
            intent.setAction(REPLY_ACTION);
            intent.putExtra(KEY_NOTIFICATION_ID, mNotificationId);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            pendingIntent = PendingIntent.getActivity(this, 100, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
        }


        // Create the reply action and add the remote input.
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT_WATCH) {
            NotificationCompat.Action action =
                    new NotificationCompat.Action.Builder(R.drawable.ic_send_black_24dp,
                            getString(R.string.reply_label), pendingIntent)
                            .addRemoteInput(remoteInput)
                            .build();
            mBuilder.addAction(action);
        }


        if (mNotifyMgr != null) {
            mNotifyMgr.notify(mNotificationId, mBuilder.build());
        }


    }
}
