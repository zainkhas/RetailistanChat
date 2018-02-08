package com.technologyleaks.retailistanchat.login.model;

import com.technologyleaks.retailistanchat.login.MVP_Login;

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


}
