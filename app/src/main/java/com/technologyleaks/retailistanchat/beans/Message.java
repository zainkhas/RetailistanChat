package com.technologyleaks.retailistanchat.beans;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by zainulabideen on 08/02/2018.
 */

@IgnoreExtraProperties
public class Message {

    //Table Name
    public static final String TABLENAME = "messages";


    //Columns
    public static final String COLUMN_USER_ID = "userid";
    public static final String COLUMN_TEXT = "text";
    public static final String COLUMN_TIME = "time";
    public static final String COLUMN_SENDER_NAME = "sendername";

    @Exclude
    private String key;
    private String userId;
    private String text;
    private String time;
    private String senderName;


    public Message() {
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }
}
