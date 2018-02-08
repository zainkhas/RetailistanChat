package com.technologyleaks.retailistanchat.login.presenter;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.technologyleaks.retailistanchat.login.MVP_Login;

import java.lang.ref.WeakReference;

/**
 * Created by Shahzore on 08-Feb-18.
 */

public class LoginPresenter implements MVP_Login.ViewToPresenter, MVP_Login.ModelToPresenter {


    private static final String TAG = LoginPresenter.class.getSimpleName();

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

}
