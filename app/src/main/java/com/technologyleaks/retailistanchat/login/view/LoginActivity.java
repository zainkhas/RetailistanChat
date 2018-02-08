package com.technologyleaks.retailistanchat.login.view;

import com.technologyleaks.retailistanchat.R;
import com.technologyleaks.retailistanchat.base.view.BaseActivity;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import com.technologyleaks.retailistanchat.base.presenter.BasePresenter;
import com.technologyleaks.retailistanchat.commons.StateMaintainer;
import com.technologyleaks.retailistanchat.login.MVP_Login;
import com.technologyleaks.retailistanchat.login.model.LoginModel;
import com.technologyleaks.retailistanchat.login.presenter.LoginPresenter;

/**
 * Created by Shahzore on 08-Feb-18.
 */

public class LoginActivity extends BaseActivity implements MVP_Login.PresenterToView {

    private static final String TAG = LoginActivity.class.getSimpleName();

    private final StateMaintainer mStateMaintainer =
            new StateMaintainer(getSupportFragmentManager(), BaseActivity.class.getName());


    /* Presenter */
    private MVP_Login.ViewToPresenter mPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setUpViews();
        setUpMVP();
    }

    private void setUpViews() {
    }

    private void setUpMVP() {
        // Check if StateMaintainer has been created
        if (mStateMaintainer.firstTimeIn() || mStateMaintainer.wasRecreated()) {
            // Create the Presenter
            LoginPresenter presenter = new LoginPresenter(this);
            // Create the Model
            LoginModel model = new LoginModel(presenter);
            // Set Presenter model
            presenter.setModel(model);
            // Add Presenter and Model to StateMaintainer
            mStateMaintainer.put(presenter);
            mStateMaintainer.put(model);

            // Set the Presenter as a interface
            // To limit the communication with it
            mPresenter = presenter;

        }
        // get the Presenter from StateMaintainer
        else {
            // Get the Presenter
            mPresenter = mStateMaintainer.get(BasePresenter.class.getName());
            // Updated the View in Presenter
            mPresenter.setView(this);
        }
    }

    @Override
    public Context getAppContext() {
        return this.getApplicationContext();
    }

    @Override
    public Context getActivityContext() {
        return this;
    }

    @Override
    public void showToast(Toast toast) {
        toast.show();
    }


}
