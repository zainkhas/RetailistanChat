package com.technologyleaks.retailistanchat.register.view;

import com.technologyleaks.retailistanchat.R;
import com.technologyleaks.retailistanchat.base.view.BaseActivity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.technologyleaks.retailistanchat.base.presenter.BasePresenter;
import com.technologyleaks.retailistanchat.commons.StateMaintainer;
import com.technologyleaks.retailistanchat.login.MVP_Login;
import com.technologyleaks.retailistanchat.login.model.LoginModel;
import com.technologyleaks.retailistanchat.login.presenter.LoginPresenter;
import com.technologyleaks.retailistanchat.register.MVP_Register;
import com.technologyleaks.retailistanchat.register.model.RegisterModel;
import com.technologyleaks.retailistanchat.register.presenter.RegisterPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Shahzore on 08-Feb-18.
 */

public class RegisterActivity extends AppCompatActivity implements MVP_Register.PresenterToView {

    private static final String TAG = RegisterActivity.class.getSimpleName();

    private final StateMaintainer mStateMaintainer =
            new StateMaintainer(getSupportFragmentManager(), BaseActivity.class.getName());


    /* Presenter */
    private MVP_Register.ViewToPresenter mPresenter;

    /* Views */
    @BindView(R.id.editText_username)
    protected EditText editText_username;
    @BindView(R.id.editText_type_password)
    protected EditText editText_type_password;
    @BindView(R.id.editText_re_type_password)
    protected EditText editText_re_type_password;
    @BindView(R.id.button_login)
    protected Button button_login;
    @BindView(R.id.progressLayout)
    protected FrameLayout progressLayout;
    @BindView(R.id.contentLayout)
    protected LinearLayout contentLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        setUpViews();
        setUpMVP();
    }

    private void setUpViews() {
    }

    private void setUpMVP() {
        // Check if StateMaintainer has been created
        if (mStateMaintainer.firstTimeIn() || mStateMaintainer.wasRecreated()) {
            // Create the Presenter
            RegisterPresenter presenter = new RegisterPresenter(this);
            // Create the Model
            RegisterModel model = new RegisterModel(presenter);
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

    @Override
    public void showProgress() {
        progressLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressLayout.setVisibility(View.GONE);
    }

    @OnClick(R.id.button_login)
    void onLoginButtonClicked() {
        mPresenter.onLoginButtonClicked(editText_username, editText_type_password, editText_re_type_password, button_login);
    }

}
