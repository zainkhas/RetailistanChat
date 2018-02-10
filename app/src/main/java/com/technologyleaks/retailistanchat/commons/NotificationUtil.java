package com.technologyleaks.retailistanchat.commons;

import android.content.Context;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.technologyleaks.retailistanchat.R;

/**
 * Created by zainulabideen on 10/02/2018.
 */

public class NotificationUtil {
    public static final int mNotificationId = 001;
    public static final String REPLY_ACTION = "REPLY_ACTION";
    public static final String KEY_NOTIFICATION_ID = "KEY_NOTIFICATION_ID";
    public static final String KEY_TEXT_REPLY = "key_text_reply";


    //Update the notification to stop showing progressbar after sending reply
    public static void updateNotification(Context context) {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_message_white_24dp_1x)
                .setContentText(context.getString(R.string.notif_content_sent));

        notificationManager.notify(mNotificationId, builder.build());
    }

}
