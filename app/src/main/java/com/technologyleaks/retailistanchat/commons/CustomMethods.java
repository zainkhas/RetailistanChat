package com.technologyleaks.retailistanchat.commons;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.view.inputmethod.InputMethodManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by zainulabideen on 08/02/2018.
 */

public class CustomMethods {
    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;

    public static void hideSoftKeyboardDialogDismiss(final Activity activity) {
        new Handler().postDelayed(() -> activity.runOnUiThread(() -> {
            InputMethodManager inputMethodManager = (InputMethodManager) activity
                    .getSystemService(Activity.INPUT_METHOD_SERVICE);
            if (null != activity.getCurrentFocus()) {
                if (inputMethodManager != null) {
                    inputMethodManager.hideSoftInputFromWindow(activity
                            .getCurrentFocus().getWindowToken(), 0);
                }
            }
        }), 1);
    }

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            return cm.getActiveNetworkInfo() != null;
        }
        return false;
    }


    public static String getTimeAgo(long time) {

        //if timestamp given in seconds, convert to millis time *= 1000; }
        long now = System.currentTimeMillis();
        if (time > now || time <= 0) {
            return "";
        }

        long diff = now - time;

        if (diff < MINUTE_MILLIS) {
            return "Just now";
        } else if (diff < 2 * MINUTE_MILLIS) {
            return "1 min";
        } else if (diff < 50 * MINUTE_MILLIS) {
            return diff / MINUTE_MILLIS + " mins";
        } else if (diff < 90 * MINUTE_MILLIS) {
            return "1 hour";
        } else if (diff < 24 * HOUR_MILLIS) {
            return diff / HOUR_MILLIS + " hours";
        } else if (diff < 48 * HOUR_MILLIS) {
            return "Yesterday";
        } else {
            return new SimpleDateFormat("d/M/yy", Locale.US).format(new Date(time));
        }

    }
}
