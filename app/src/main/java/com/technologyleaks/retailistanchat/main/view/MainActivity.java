package com.technologyleaks.retailistanchat.main.view;

import android.app.Activity;
import android.arch.lifecycle.LifecycleOwner;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.messaging.FirebaseMessaging;
import com.technologyleaks.retailistanchat.R;
import com.technologyleaks.retailistanchat.base.presenter.BasePresenter;
import com.technologyleaks.retailistanchat.base.view.BaseActivity;
import com.technologyleaks.retailistanchat.commons.CustomMethods;
import com.technologyleaks.retailistanchat.commons.StateMaintainer;
import com.technologyleaks.retailistanchat.main.MVP_Main;
import com.technologyleaks.retailistanchat.main.helpers.ActivityFragmentContract;
import com.technologyleaks.retailistanchat.main.model.MainModel;
import com.technologyleaks.retailistanchat.main.presnter.MainPresenter;

import butterknife.BindView;
import butterknife.OnClick;
import cn.nekocode.emojix.Emojix;

public class MainActivity extends BaseActivity implements MVP_Main.PresenterToView, View.OnClickListener, ActivityFragmentContract.FragmentToActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private final StateMaintainer mStateMaintainer =
            new StateMaintainer(getSupportFragmentManager(), MainActivity.class.getName());


    /* Presenter */
    private MVP_Main.ViewToPresenter mPresenter;

    /* Views */
    @BindView(R.id.editTex_message)
    protected EditText editTex_message;
    @BindView(R.id.button_send)
    protected ImageButton button_send;
    @BindView(R.id.recyclerView)
    protected RecyclerView recyclerView;
    private TextView textView_onlineUsers;
    private BottomSheetFragment bottomSheetFragment = new BottomSheetFragment();


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(Emojix.wrap(newBase));
//        Emoji support
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseMessaging.getInstance().subscribeToTopic("alerts");

        setUpViews();
        setUpMVP();

        mPresenter.getOnlineUsersCount();

    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.checkLogin();
        mPresenter.populateRecyclerView(recyclerView, this);
    }


    @Override
    protected void onStart() {
        super.onStart();
        mPresenter.takeOnline();
    }

    @Override
    protected void onStop() {
        mPresenter.takeOffline();
        super.onStop();
    }

    @Override
    protected void onPause() {
        mPresenter.takeOffline();
        super.onPause();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {


        //Get a reference to your item by id
        MenuItem item = menu.findItem(R.id.action_online);


        //Then you access to your control by finding it in the rootView
        RelativeLayout onlineUsersLayout = (RelativeLayout) item.getActionView();
        onlineUsersLayout.setOnClickListener(this);

        textView_onlineUsers = onlineUsersLayout.findViewById(R.id.textView_onlineUsers);

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            mPresenter.onMenuLogoutSelected();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setUpMVP() {
        // Check if StateMaintainer has been created
        if (mStateMaintainer.firstTimeIn() || mStateMaintainer.wasRecreated()) {
            // Create the Presenter
            MainPresenter presenter = new MainPresenter(this);
            // Create the Model
            MainModel model = new MainModel(presenter);
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

    private void setUpViews() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        editTex_message.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length() > 0) {
                    button_send.setClickable(true);
                    button_send.setBackground(ContextCompat.getDrawable(getActivityContext(), R.drawable.send_button_background));
                } else {
                    button_send.setClickable(false);
                    button_send.setBackground(ContextCompat.getDrawable(getActivityContext(), R.drawable.send_button_background_disabled));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        editTex_message.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                // hide virtual keyboard
                hideKeyboard(editTex_message);
                mPresenter.onButtonSendClicked(editTex_message);
                button_send.setClickable(false);
                button_send.setBackground(ContextCompat.getDrawable(getActivityContext(), R.drawable.send_button_background_disabled));
                return true;
            }
            return false;
        });
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    @OnClick(R.id.button_send)
    void onButtonSendClicked() {
        mPresenter.onButtonSendClicked(editTex_message);
    }

    @Override
    public void clearMessage() {
        editTex_message.setText("");
        editTex_message.clearFocus();
        button_send.setClickable(false);
        button_send.setBackground(ContextCompat.getDrawable(getActivityContext(), R.drawable.send_button_background_disabled));
        CustomMethods.hideSoftKeyboardDialogDismiss(this);
    }

    @Override
    public void updateOnlineUserCount(long count) {
        textView_onlineUsers.setText(count + " online");
        bottomSheetFragment.updateTitle(count + " Online Users");
    }


    @Override
    protected boolean useDrawerToggle() {
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_onlineUsers:

                bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());


                break;
        }
    }

    @Override
    public void populateRecyclerView(RecyclerView recyclerView, LifecycleOwner lifecycleOwner) {
        mPresenter.populateOnlineUsersRecyclerView(recyclerView, lifecycleOwner);
    }
}
