package com.technologyleaks.retailistanchat.commons;

import android.app.Activity;
import android.os.Handler;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by zainulabideen on 08/02/2018.
 */

public class CustomMethods {

    public static void hideSoftKeyboardDialogDismiss(final Activity activity) {
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                activity.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        InputMethodManager inputMethodManager = (InputMethodManager) activity
                                .getSystemService(Activity.INPUT_METHOD_SERVICE);
                        if (null != activity.getCurrentFocus()) {
                            inputMethodManager.hideSoftInputFromWindow(activity
                                    .getCurrentFocus().getWindowToken(), 0);
                        }
                    }
                });
            }
        }, 1);
    }
}
