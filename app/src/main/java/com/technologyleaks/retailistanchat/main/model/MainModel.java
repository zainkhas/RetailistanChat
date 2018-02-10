package com.technologyleaks.retailistanchat.main.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.technologyleaks.retailistanchat.MyApplication;
import com.technologyleaks.retailistanchat.beans.Message;
import com.technologyleaks.retailistanchat.beans.NotificationQueue;
import com.technologyleaks.retailistanchat.commons.CustomMethods;
import com.technologyleaks.retailistanchat.commons.PushNotificationHelper;
import com.technologyleaks.retailistanchat.commons.SharedPrefs;
import com.technologyleaks.retailistanchat.dao.NotificationQueueTableHelper;
import com.technologyleaks.retailistanchat.main.MVP_Main;

import io.reactivex.Flowable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainModel implements MVP_Main.PresenterToModel {


    private static final String TAG = MainModel.class.getSimpleName();

    // Presenter reference
    private MVP_Main.ModelToPresenter mPresenter;
    private DatabaseReference mChatIndicesRef;
    private NotificationQueueTableHelper notificationQueueTableHelper;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();


    /**
     * Main constructor, called by Activity during MVP setup
     *
     * @param presenter Presenter instance
     */
    public MainModel(MVP_Main.ModelToPresenter presenter) {
        this.mPresenter = presenter;
        this.mChatIndicesRef = FirebaseDatabase.getInstance()
                .getReference(Message.TABLENAME);
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

        String messageId = mChatIndicesRef.push().getKey();

        Message messageObj = new Message();
        messageObj.setText(message);
        messageObj.setUserId(SharedPrefs.getUserId());
        messageObj.setTime(String.valueOf(System.currentTimeMillis()));
        messageObj.setSenderName(SharedPrefs.getUserName());

        mChatIndicesRef.child(messageId).setValue(messageObj, (databaseError, databaseReference) -> {
            if (databaseError == null) {
                //success

                if (CustomMethods.isNetworkConnected(mPresenter.getAppContext())) {

                    Disposable disposable = Flowable.just(notificationQueueTableHelper)
                            .subscribeOn(Schedulers.io())
                            .subscribe(table -> {
                                NotificationQueue queue = new NotificationQueue();
                                queue.setUsername(SharedPrefs.getUserName());
                                queue.setMessage(message);

                                table.addNotificationQueue(queue);

                                PushNotificationHelper.sendAll(table.getAll());
                            });

                    compositeDisposable.add(disposable);


                }

            }
        });
        mPresenter.onMessageSend();

    }


}
