package com.technologyleaks.retailistanchat.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.RemoteInput;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.technologyleaks.retailistanchat.R;
import com.technologyleaks.retailistanchat.main.view.MainActivity;

/**
 * Created by Shahzore on 09-Feb-18.
 */

public class FirebaseMessageService extends FirebaseMessagingService {
    public static String TAG = FirebaseMessageService.class.getSimpleName();
    private int mNotificationId = 001;
    // Key for the string that's delivered in the action's intent.
    private static final String KEY_TEXT_REPLY = "key_text_reply";
    private static final int REPLY_REQUEST_CODE = 3424;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // ...

        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }


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
                        .setContentTitle(remoteMessage.getNotification().getTitle())
                        .setContentText(remoteMessage.getNotification().getBody());


        Intent resultIntent = new Intent(this, MainActivity.class);


        // Build a PendingIntent for the reply action to trigger.
        PendingIntent replyPendingIntent =
                PendingIntent.getBroadcast(getApplicationContext(),
                        REPLY_REQUEST_CODE,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);


//        PendingIntent resultPendingIntent =
//                PendingIntent.getActivity(
//                        this,
//                        0,
//                        resultIntent,
//                        PendingIntent.FLAG_UPDATE_CURRENT
//                );

        // mBuilder.setContentIntent(resultPendingIntent);


        // Create the reply action and add the remote input.
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT_WATCH) {
            NotificationCompat.Action action =
                    new NotificationCompat.Action.Builder(R.drawable.ic_send_black_24dp,
                            getString(R.string.reply_label), replyPendingIntent)
                            .addRemoteInput(remoteInput)
                            .build();
            mBuilder.addAction(action);
        }


        if (mNotifyMgr != null) {
            mNotifyMgr.notify(mNotificationId, mBuilder.build());
        }


    }
}
