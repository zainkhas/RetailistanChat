package com.technologyleaks.retailistanchat.login.presenter;

import android.content.Context;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.technologyleaks.retailistanchat.R;
import com.technologyleaks.retailistanchat.commons.Navigator;
import com.technologyleaks.retailistanchat.commons.SharedPrefs;
import com.technologyleaks.retailistanchat.login.MVP_Login;

import java.lang.ref.WeakReference;

/**
 * Created by Shahzore on 08-Feb-18.
 */

public class LoginPresenter implements MVP_Login.ViewToPresenter, MVP_Login.ModelToPresenter {

    private static final String TAG = LoginPresenter.class.getSimpleName();

    private DatabaseReference mDatabase;


    // View reference. We use as a WeakReference
    // because the Activity could be destroyed at any time
    // and we don't want to create a memory leak
    private WeakReference<MVP_Login.PresenterToView> mView;
    // Model reference
    private MVP_Login.PresenterToModel mModel;

    /**
     * Presenter Constructor
     *
     * @param view MainActivity
     */
    public LoginPresenter(MVP_Login.PresenterToView view) {
        mView = new WeakReference<>(view);
        mDatabase = FirebaseDatabase.getInstance().getReference();
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
    private MVP_Login.PresenterToView getView() throws NullPointerException {
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
    public void setView(MVP_Login.PresenterToView view) {
        mView = new WeakReference<>(view);
    }


    /**
     * Called by Activity during MVP setup. Only called once.
     *
     * @param model Model instance
     */
    public void setModel(MVP_Login.PresenterToModel model) {
        mModel = model;

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


    /**
     * Custom Methods
     **/


    @Override
    public void onLoginButtonClicked(EditText editText_username, EditText editText_password, Button button_login) {

        String username = editText_username.getText().toString();
        final String password = editText_password.getText().toString();

        String errorMessage = "";
        boolean isValid = false;

        if (username.length() < 1) {
            errorMessage = getAppContext().getString(R.string.please_enter_username);
        } else if (editText_password.length() < 1) {
            errorMessage = getAppContext().getString(R.string.please_enter_password);
        } else {
            isValid = true;
        }


        if (isValid) {
            mModel.performLogin(username, password);
        } else {
            getView().showToast(makeToast(errorMessage));
        }

    }

    @Override
    public void onRegisterClicked() {
        Navigator.navigate(getActivityContext(), Navigator.SCREEN.REGISTER);
    }


}
