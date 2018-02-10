package com.technologyleaks.retailistanchat;

import android.app.Application;
import android.arch.persistence.room.Room;
import android.content.ContextWrapper;

import com.androidnetworking.AndroidNetworking;
import com.google.firebase.database.FirebaseDatabase;
import com.pixplicity.easyprefs.library.Prefs;
import com.technologyleaks.retailistanchat.dao.AppDatabase;

/**
 * Created by zainulabideen on 08/02/2018.
 */

public class MyApplication extends Application {

    private AppDatabase appDatabase;

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        new Prefs.Builder()
                .setContext(this)
                .setMode(ContextWrapper.MODE_PRIVATE)
                .setPrefsName(getPackageName())
                .setUseDefaultSharedPreference(true)
                .build();

        AndroidNetworking.initialize(getApplicationContext());

        appDatabase = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "RetailistanChat.db").build();
    }


    public AppDatabase getAppDatabase() {
        return appDatabase;
    }
}
