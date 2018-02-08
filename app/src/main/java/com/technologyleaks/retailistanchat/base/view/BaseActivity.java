package com.technologyleaks.retailistanchat.base.view;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.technologyleaks.retailistanchat.R;
import com.technologyleaks.retailistanchat.base.MVP_Base;
import com.technologyleaks.retailistanchat.base.model.BaseModel;
import com.technologyleaks.retailistanchat.base.presenter.BasePresenter;
import com.technologyleaks.retailistanchat.commons.StateMaintainer;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zainulabideen on 07/02/2018.
 */

public class BaseActivity extends AppCompatActivity implements MVP_Base.PresenterToView {

    private static final String TAG = BaseActivity.class.getSimpleName();

    private final StateMaintainer mStateMaintainer =
            new StateMaintainer(getSupportFragmentManager(), BaseActivity.class.getName());


    /* Views */
    @BindView(R.id.nav_view)
    protected NavigationView navigationView;
    @BindView(R.id.drawer_layout)
    protected DrawerLayout drawerLayout;
    @BindView(R.id.toolbar)
    protected Toolbar toolbar;


    /* Presenter */
    private MVP_Base.ViewToPresenter mPresenter;


    private ActionBarDrawerToggle actionBarDrawerToggle;
    private boolean showUpButton = false;


    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        /**
         * This is going to be our actual root layout.
         */
        drawerLayout = (DrawerLayout) getLayoutInflater().inflate(R.layout.activity_base, null);
        /**
         * {@link FrameLayout} to inflate the child's view. We could also use a {@link android.view.ViewStub}
         */
        FrameLayout activityContainer = drawerLayout.findViewById(R.id.activity_content);
        getLayoutInflater().inflate(layoutResID, activityContainer, true);

        /**
         * Note that we don't pass the child's layoutId to the parent,
         * instead we pass it our inflated layout.
         */
        super.setContentView(drawerLayout);

        ButterKnife.bind(this);
        setUpViews();
        setUpNavView();
        setUpMVP();

    }


    private void setUpViews() {
        if (useToolbar()) {
            setSupportActionBar(toolbar);
        } else {
            toolbar.setVisibility(View.GONE);
        }
    }

    protected void setUpNavView() {

        if (useDrawerToggle()) { // use the hamburger menu
            actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                    R.string.navigation_drawer_open,
                    R.string.navigation_drawer_close) {
            };

            drawerLayout.setDrawerListener(actionBarDrawerToggle);
            actionBarDrawerToggle.syncState();
        } else if (useToolbar() && getSupportActionBar() != null && showUpButton) {

            // Use home/back button instead
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(getResources()
                    .getDrawable(R.drawable.ic_arrow_back_black_24dp));
        }


        //Lock/Unlock drawer
        if (useDrawerToggle()) {
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        } else {
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }
    }


    private void setUpMVP() {
        // Check if StateMaintainer has been created
        if (mStateMaintainer.firstTimeIn() || mStateMaintainer.wasRecreated()) {
            // Create the Presenter
            BasePresenter presenter = new BasePresenter(this);
            // Create the Model
            BaseModel model = new BaseModel(presenter);
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


    /**
     * Helper method that can be used by child classes to
     * specify that they don't want a {@link Toolbar}
     *
     * @return true
     */


    protected boolean useToolbar() {
        return true;
    }

    protected boolean useToolbar(boolean showUpButton) {
        this.showUpButton = showUpButton;
        return true;
    }

    /**
     * Helper method to allow child classes to opt-out of having the
     * hamburger menu.
     *
     * @return
     */
    protected boolean useDrawerToggle() {
        return true;
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
