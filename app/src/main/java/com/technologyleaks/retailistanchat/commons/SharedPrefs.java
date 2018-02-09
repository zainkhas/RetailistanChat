package com.technologyleaks.retailistanchat.commons;

import com.pixplicity.easyprefs.library.Prefs;

/**
 * Created by zainulabideen on 08/02/2018.
 */

public class SharedPrefs {

    /* Constants */
    private static final String IS_LOGGED_IN = "IS_LOGGED_IN";
    private static final String USER_ID = "USER_ID";
    private static final String USER_NAME = "USER_NAME";


    public static boolean isLoggedIn() {
        return Prefs.getBoolean(IS_LOGGED_IN, false);
    }

    public static void setIsLoggedIn(boolean value) {
        Prefs.putBoolean(IS_LOGGED_IN, value);
    }


    public static String getUserId() {
        return Prefs.getString(USER_ID, "");
    }

    public static void setUserId(String value) {
        Prefs.putString(USER_ID, value);
    }

    public static String getUserName() {
        return Prefs.getString(USER_NAME, "");
    }

    public static void setUserName(String value) {
        Prefs.putString(USER_NAME, value);
    }

}
