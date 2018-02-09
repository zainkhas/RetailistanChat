package com.technologyleaks.retailistanchat.register.presenter;

import android.content.Context;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.technologyleaks.retailistanchat.beans.User;
import com.technologyleaks.retailistanchat.commons.Navigator;
import com.technologyleaks.retailistanchat.commons.SharedPrefs;
import com.technologyleaks.retailistanchat.register.MVP_Register;

import java.lang.ref.WeakReference;

/**
 * Created by Shahzore on 08-Feb-18.
 */

public class RegisterPresenter implements MVP_Register.ViewToPresenter, MVP_Register.ModelToPresenter {


    private static final String TAG = RegisterPresenter.class.getSimpleName();

    // View reference. We use as a WeakReference
    // because the Activity could be destroyed at any time
    // and we don't want to create a memory leak
    private WeakReference<MVP_Register.PresenterToView> mView;
    // Model reference
    private MVP_Register.PresenterToModel mModel;

    /**
     * Presenter Constructor
     *
     * @param view MainActivity
     */
    public RegisterPresenter(MVP_Register.PresenterToView view) {
        mView = new WeakReference<>(view);
    }

    /**
     * Called by View every time it is destroyed.
     *
     * @param isChangingConfiguration true: is changing configuration
     *                                and will be recreated
     */
    @Override
    public void onDestroy(boolean isChangingConfiguration) {
        // View show be null every time onDestroy is called
        mView = null;
        // Inform Model about the event
        mModel.onDestroy(isChangingConfiguration);
        // Activity destroyed
        if (!isChangingConfiguration) {
            // Nulls Model when the Activity destruction is permanent
            mModel = null;
        }
    }

    /**
     * Return the View reference.
     * Could throw an exception if the View is unavailable.
     *
     * @return {@link .RequiredViewOps}
     * @throws NullPointerException when View is unavailable
     */
    private MVP_Register.PresenterToView getView() throws NullPointerException {
        if (mView != null)
            return mView.get();
        else
            throw new NullPointerException("View is unavailable");
    }

    /**
     * Called by View during the reconstruction events
     *
     * @param view Activity instance
     */
    @Override
    public void setView(MVP_Register.PresenterToView view) {
        mView = new WeakReference<>(view);
    }


    /**
     * Called by Activity during MVP setup. Only called once.
     *
     * @param model Model instance
     */
    public void setModel(MVP_Register.PresenterToModel model) {
        mModel = model;

    }

    @Override
    public void onLoginButtonClicked(EditText editText_username, EditText editText_type_password, EditText editText_re_type_password, Button button_login) {

        String errorMessage = "";
        String userName = "";
        String userPassword = "";
        String confirmPassword = "";

        userName = editText_username.getText().toString();
        userPassword = editText_type_password.getText().toString();
        confirmPassword = editText_re_type_password.getText().toString();

        boolean isValid = false;

        if (editText_username.getText().length() < 1) {
            errorMessage = "Please enter Username!";
        } else if (editText_type_password.getText().length() < 1) {
            errorMessage = "Please enter Password!";
        } else if (editText_re_type_password.getText().length() < 1) {
            errorMessage = "Please re-enter Password!";
        } else if (!userPassword.equals(confirmPassword)) {
            errorMessage = "Both Passwords should be same";
        } else {
            isValid = true;
        }


        if (isValid) {
            mModel.saveRegisterData(userName, userPassword);
        } else {
            getView().showToast(makeToast(errorMessage));
        }
    }

    /**
     * Creat a Toast object with given message
     *
     * @param msg Toast message
     * @return A Toast object
     */
    private Toast makeToast(String msg) {
        return Toast.makeText(getActivityContext(), msg, Toast.LENGTH_SHORT);
    }

    @Override
    public void onLoginError(String errorMessage) {
        getView().showToast(makeToast(errorMessage));
    }

    @Override
    public void onLoginSuccess(String userId, String userName) {
        SharedPrefs.setIsLoggedIn(true);
        SharedPrefs.setUserId(userId);
        SharedPrefs.setUserName(userName);
        Navigator.navigate(getActivityContext(), Navigator.SCREEN.MAIN, true);
    }

    @Override
    public Context getAppContext() {
        try {
            return getView().getAppContext();
        } catch (NullPointerException e) {
            return null;
        }
    }

    /**
     * Retrieves Activity context
     *
     * @return Activity context
     */
    @Override
    public Context getActivityContext() {
        try {
            return getView().getActivityContext();
        } catch (NullPointerException e) {
            return null;
        }
    }

}
