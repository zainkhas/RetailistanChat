package com.technologyleaks.retailistanchat.base.model;

import com.technologyleaks.retailistanchat.base.MVP_Base;

public class BaseModel implements MVP_Base.PresenterToModel {


    private static final String TAG = BaseModel.class.getSimpleName();

    // Presenter reference
    private MVP_Base.ModelToPresenter mPresenter;


    /**
     * Main constructor, called by Activity during MVP setup
     *
     * @param presenter Presenter instance
     */
    public BaseModel(MVP_Base.ModelToPresenter presenter) {
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
