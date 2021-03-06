package com.technologyleaks.retailistanchat.main.model;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.technologyleaks.retailistanchat.MyApplication;
import com.technologyleaks.retailistanchat.beans.OnlineUsers;
import com.technologyleaks.retailistanchat.commons.MessageSender;
import com.technologyleaks.retailistanchat.commons.SharedPrefs;
import com.technologyleaks.retailistanchat.dao.NotificationQueueTableHelper;
import com.technologyleaks.retailistanchat.main.MVP_Main;

import java.util.ArrayList;

import io.reactivex.disposables.CompositeDisposable;

public class MainModel implements MVP_Main.PresenterToModel {


    private static final String TAG = MainModel.class.getSimpleName();

    // Presenter reference
    private MVP_Main.ModelToPresenter mPresenter;
    private DatabaseReference mDatabase;
    private NotificationQueueTableHelper notificationQueueTableHelper;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private OnlineUsers onlineUsers;


    /**
     * Main constructor, called by Activity during MVP setup
     *
     * @param presenter Presenter instance
     */
    public MainModel(MVP_Main.ModelToPresenter presenter) {
        this.mPresenter = presenter;
        this.mDatabase = FirebaseDatabase.getInstance().getReference().child(OnlineUsers.TABLENAME);
        this.mDatabase.keepSynced(true);
        this.notificationQueueTableHelper = new NotificationQueueTableHelper(((MyApplication) mPresenter.getAppContext()).getAppDatabase());
    }


    /**
     * Called by Presenter when View is destroyed
     *
     * @param isChangingConfiguration true configuration is changing
     */
    @Override
    public void onDestroy(boolean isChangingConfiguration) {

        if (compositeDisposable != null && !compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
        }

        if (!isChangingConfiguration) {
            mPresenter = null;
        }
    }

    @Override
    public void sendMessage(final String message) {

        MessageSender.send(
                mPresenter.getAppContext(),
                message,
                notificationQueueTableHelper
        );


        if (onlineUsers != null) {
            //update last activity time
            onlineUsers.setLast_update_time(String.valueOf(System.currentTimeMillis()));
            mDatabase.child(onlineUsers.getKey()).setValue(onlineUsers);
        }

        mPresenter.onMessageSend();

    }

    @Override
    public void takeOnline() {
        Query onlineCheckQuery = FirebaseDatabase.getInstance().getReference()
                .child(OnlineUsers.TABLENAME)
                .orderByChild(OnlineUsers.COLUMN_USER_NAME)
                .equalTo(SharedPrefs.getUserName())
                .limitToFirst(1);


        onlineCheckQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                ArrayList<OnlineUsers> mOnlineUsers = new ArrayList<>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    OnlineUsers onlineUsers = snapshot.getValue(OnlineUsers.class);
                    if (onlineUsers != null) {
                        onlineUsers.setKey(snapshot.getKey());
                    }
                    mOnlineUsers.add(onlineUsers);
                }


                if (mOnlineUsers.size() > 0) {
                    //This user is already online
                    onlineUsers = mOnlineUsers.get(0);

                } else {
                    String onlineUserKey = mDatabase.push().getKey();
                    onlineUsers = new OnlineUsers();
                    onlineUsers.setKey(onlineUserKey);

                }

                onlineUsers.setUsername(SharedPrefs.getUserName());
                onlineUsers.setLast_update_time(String.valueOf(System.currentTimeMillis()));
                mDatabase.child(onlineUsers.getKey()).setValue(onlineUsers);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, databaseError.getDetails());

            }
        });
    }

    @Override
    public void takeOffline() {
        if (onlineUsers != null) {
            mDatabase.child(onlineUsers.getKey()).removeValue();
        }
    }

    @Override
    public void getOnlineUsersCount() {
        Query onlineCheckQuery = FirebaseDatabase.getInstance().getReference()
                .child(OnlineUsers.TABLENAME);
        onlineCheckQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long onlineCount = dataSnapshot.getChildrenCount();
                mPresenter.onOnlineCountUpdate(onlineCount);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


}
