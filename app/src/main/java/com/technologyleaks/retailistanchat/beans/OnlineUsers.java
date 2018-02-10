package com.technologyleaks.retailistanchat.beans;

import com.google.firebase.database.Exclude;

/**
 * Created by zainulabideen on 10/02/2018.
 */

public class OnlineUsers {

    //Table Name
    public static final String TABLENAME = "onlineusers";


    //Columns
    public static final String COLUMN_USER_NAME = "username";
    public static final String COLUMN_LAST_UPDATE_TIME = "lastupdatetime";


    @Exclude
    private String key;
    private String username;
    private String last_update_time;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLast_update_time() {
        return last_update_time;
    }

    public void setLast_update_time(String last_update_time) {
        this.last_update_time = last_update_time;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
