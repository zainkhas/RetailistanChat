package com.technologyleaks.retailistanchat.commons;

import com.pixplicity.easyprefs.library.Prefs;

/**
 * Created by zainulabideen on 08/02/2018.
 */

public class SharedPrefs {

    /* Constants */
    private static final String IS_LOGGED_IN = "IS_LOGGED_IN";


    public static boolean isLoggedIn() {
        return Prefs.getBoolean(IS_LOGGED_IN, false);
    }

    public static void setIsLoggedIn(boolean value) {
        Prefs.putBoolean(IS_LOGGED_IN, value);
    }

}
