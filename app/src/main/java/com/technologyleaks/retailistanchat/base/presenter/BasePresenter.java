package com.technologyleaks.retailistanchat.base.presenter;

import android.content.Context;
import android.widget.Toast;

import com.technologyleaks.retailistanchat.base.MVP_Base;

import java.lang.ref.WeakReference;


public class BasePresenter implements MVP_Base.ViewToPresenter, MVP_Base.ModelToPresenter {


    private static final String TAG = BasePresenter.class.getSimpleName();

    // View reference. We use as a WeakReference
    // because the Activity could be destroyed at any time
    // and we don't want to create a memory leak
    private WeakReference<MVP_Base.PresenterToView> mView;
    // Model reference
    private MVP_Base.PresenterToModel mModel;

    /**
     * Presenter Constructor
     *
     * @param view MainActivity
     */
    public BasePresenter(MVP_Base.PresenterToView view) {
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
    private MVP_Base.PresenterToView getView() throws NullPointerException {
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
    public void setView(MVP_Base.PresenterToView view) {
        mView = new WeakReference<>(view);
    }


    /**
     * Called by Activity during MVP setup. Only called once.
     *
     * @param model Model instance
     */
    public void setModel(MVP_Base.PresenterToModel model) {
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
