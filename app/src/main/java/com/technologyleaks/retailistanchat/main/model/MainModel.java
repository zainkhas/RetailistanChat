package com.technologyleaks.retailistanchat.main.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.technologyleaks.retailistanchat.MyApplication;
import com.technologyleaks.retailistanchat.beans.Message;
import com.technologyleaks.retailistanchat.commons.MessageSender;
import com.technologyleaks.retailistanchat.dao.NotificationQueueTableHelper;
import com.technologyleaks.retailistanchat.main.MVP_Main;

import io.reactivex.disposables.CompositeDisposable;

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

        MessageSender.send(
                mPresenter.getAppContext(),
                message,
                notificationQueueTableHelper
        );
        mPresenter.onMessageSend();

    }


}
