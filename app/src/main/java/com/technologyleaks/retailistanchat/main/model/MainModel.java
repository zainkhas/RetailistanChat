package com.technologyleaks.retailistanchat.main.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.technologyleaks.retailistanchat.beans.Message;
import com.technologyleaks.retailistanchat.commons.SharedPrefs;
import com.technologyleaks.retailistanchat.main.MVP_Main;

public class MainModel implements MVP_Main.PresenterToModel {


    private static final String TAG = MainModel.class.getSimpleName();

    // Presenter reference
    private MVP_Main.ModelToPresenter mPresenter;
    private DatabaseReference mChatIndicesRef;


    /**
     * Main constructor, called by Activity during MVP setup
     *
     * @param presenter Presenter instance
     */
    public MainModel(MVP_Main.ModelToPresenter presenter) {
        this.mPresenter = presenter;
        this.mChatIndicesRef = FirebaseDatabase.getInstance()
                .getReference(Message.TABLENAME);
    }


    /**
     * Called by Presenter when View is destroyed
     *
     * @param isChangingConfiguration true configuration is changing
     */
    @Override
    public void onDestroy(boolean isChangingConfiguration) {
        if (!isChangingConfiguration) {
            mPresenter = null;
        }
    }

    @Override
    public void sendMessage(String message) {

        String messageId = mChatIndicesRef.push().getKey();

        Message messageObj = new Message();
        messageObj.setText(message);
        messageObj.setUserId(SharedPrefs.getUserId());
        messageObj.setTime(String.valueOf(System.currentTimeMillis()));
        messageObj.setSenderName(SharedPrefs.getUserName());

        mChatIndicesRef.child(messageId).setValue(messageObj);
        mPresenter.onMessageSend();

    }


}
