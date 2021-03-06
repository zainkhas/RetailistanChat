package com.technologyleaks.retailistanchat.login.model;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.technologyleaks.retailistanchat.R;
import com.technologyleaks.retailistanchat.beans.User;
import com.technologyleaks.retailistanchat.login.MVP_Login;

import java.util.ArrayList;

/**
 * Created by Shahzore on 08-Feb-18.
 */

public class LoginModel implements MVP_Login.PresenterToModel {


    private static final String TAG = LoginModel.class.getSimpleName();

    // Presenter reference
    private MVP_Login.ModelToPresenter mPresenter;


    /**
     * Main constructor, called by Activity during MVP setup
     *
     * @param presenter Presenter instance
     */
    public LoginModel(MVP_Login.ModelToPresenter presenter) {
        this.mPresenter = presenter;
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
    public void performLogin(final String username, final String password) {
        Query userNameCheckQuery = FirebaseDatabase.getInstance().getReference()
                .child(User.TABLENAME)
                .orderByChild(User.COLUMN_USERNAME)
                .equalTo(username)
                .limitToFirst(1);


        userNameCheckQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Log.d(TAG, "Username query count: " + dataSnapshot.getChildrenCount());

                mPresenter.onResponse();

                ArrayList<User> mUsers = new ArrayList<>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    if (user != null) {
                        user.setKey(snapshot.getKey());
                        Log.d(TAG, "key is " + user.getKey());
                    }
                    mUsers.add(user);
                }


                if (mUsers.size() > 0) {
                    User user = mUsers.get(0);
                    if (user.getPassword().equals(password)) {
                        mPresenter.onLoginSuccess(user.getKey(), user.getUsername());
                    } else {
                        mPresenter.onLoginError(mPresenter.getAppContext().getString(R.string.incorrect_password));
                    }
                } else {
                    mPresenter.onLoginError(mPresenter.getAppContext().getString(R.string.user_doesnt_exists));
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                mPresenter.onResponse();
                mPresenter.onLoginError(mPresenter.getAppContext().getString(R.string.internet_connection_problem));
            }
        });
    }


}
