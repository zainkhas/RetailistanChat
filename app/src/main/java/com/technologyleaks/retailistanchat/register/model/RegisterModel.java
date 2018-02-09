package com.technologyleaks.retailistanchat.register.model;

import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.technologyleaks.retailistanchat.R;
import com.technologyleaks.retailistanchat.beans.User;
import com.technologyleaks.retailistanchat.register.MVP_Register;

/**
 * Created by Shahzore on 08-Feb-18.
 */

public class RegisterModel implements MVP_Register.PresenterToModel {


    private static final String TAG = RegisterModel.class.getSimpleName();
    private DatabaseReference mDatabase;
    long count = 0;

    // Presenter reference
    private MVP_Register.ModelToPresenter mPresenter;


    /**
     * Main constructor, called by Activity during MVP setup
     *
     * @param presenter Presenter instance
     */
    public RegisterModel(MVP_Register.ModelToPresenter presenter) {
        this.mPresenter = presenter;
        mDatabase = FirebaseDatabase.getInstance().getReference().child(User.TABLENAME);

    }

    @Override
    public void saveRegisterData(final String userName, final String password) {
        Query userNameCheckQuery = FirebaseDatabase.getInstance().getReference()
                .child(User.TABLENAME)
                .orderByChild(User.COLUMN_USERNAME)
                .equalTo(userName)
                .limitToFirst(1);


        userNameCheckQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                count = dataSnapshot.getChildrenCount();
                if (count > 0) {
                    mPresenter.onLoginError(mPresenter.getAppContext().getString(R.string.user_doesnt_exists));
                } else {
                    String userId = mDatabase.push().getKey();
                    Log.d(TAG, "User id is: " + userId);
                    User user = new User(userName, password);
                    mDatabase.child(userId).setValue(user);
                    mPresenter.onLoginSuccess(userId, userName);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

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


}
