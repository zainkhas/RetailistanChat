package com.technologyleaks.retailistanchat.main.presnter;

import android.arch.lifecycle.LifecycleOwner;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.technologyleaks.retailistanchat.R;
import com.technologyleaks.retailistanchat.adapters.MessageAdapter;
import com.technologyleaks.retailistanchat.adapters.OnlineUsersAdapter;
import com.technologyleaks.retailistanchat.beans.Message;
import com.technologyleaks.retailistanchat.beans.OnlineUsers;
import com.technologyleaks.retailistanchat.commons.Navigator;
import com.technologyleaks.retailistanchat.commons.SharedPrefs;
import com.technologyleaks.retailistanchat.main.MVP_Main;

import java.lang.ref.WeakReference;


public class MainPresenter implements MVP_Main.ViewToPresenter, MVP_Main.ModelToPresenter {


    private static final String TAG = MainPresenter.class.getSimpleName();

    private Query messagesQuery;

    private Query onlineUsersQuery;

    private MessageAdapter messagesAdapter;
    private OnlineUsersAdapter onlineUsersAdapter;


    // View reference. We use as a WeakReference
    // because the Activity could be destroyed at any time
    // and we don't want to create a memory leak
    private WeakReference<MVP_Main.PresenterToView> mView;
    // Model reference
    private MVP_Main.PresenterToModel mModel;

    /**
     * Presenter Constructor
     *
     * @param view MainActivity
     */
    public MainPresenter(MVP_Main.PresenterToView view) {
        mView = new WeakReference<>(view);
        this.messagesQuery = FirebaseDatabase.getInstance().getReference().child(Message.TABLENAME)
                .limitToLast(100);
        this.messagesQuery.keepSynced(true);

        this.onlineUsersQuery = FirebaseDatabase.getInstance().getReference().child(OnlineUsers.TABLENAME)
                .limitToLast(100);
        this.onlineUsersQuery.keepSynced(true);

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
    private MVP_Main.PresenterToView getView() throws NullPointerException {
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
    public void setView(MVP_Main.PresenterToView view) {
        mView = new WeakReference<>(view);
    }


    /**
     * Called by Activity during MVP setup. Only called once.
     *
     * @param model Model instance
     */
    public void setModel(MVP_Main.PresenterToModel model) {
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
    public void onMessageSend() {
        getView().clearMessage();
    }

    @Override
    public void onOnlineCountUpdate(long count) {
        getView().updateOnlineUserCount(count);
    }


    /**
     * Custom Methods
     */


    @Override
    public void checkLogin() {
        if (!SharedPrefs.isLoggedIn()) {
            Navigator.navigate(getActivityContext(), Navigator.SCREEN.LOGIN, true);
        }
    }

    @Override
    public void onButtonSendClicked(EditText editText_message) {
        String message = editText_message.getText().toString();

        if (message.trim().length() > 0) {
            mModel.sendMessage(message.trim());
        }
    }

    @Override
    public void populateRecyclerView(final RecyclerView recyclerView, LifecycleOwner lifecycleOwner) {

        if (messagesAdapter == null) {
            FirebaseRecyclerOptions<Message> options =
                    new FirebaseRecyclerOptions.Builder<Message>()
                            .setQuery(messagesQuery, Message.class)
                            .setLifecycleOwner(lifecycleOwner)
                            .build();

            messagesAdapter = new MessageAdapter(getActivityContext(), options);

            // Scroll to bottom on new messages
            messagesAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                @Override
                public void onItemRangeInserted(int positionStart, int itemCount) {
                    recyclerView.scrollToPosition(messagesAdapter.getItemCount() - 1);
                }
            });
            recyclerView.setAdapter(messagesAdapter);


        }

    }

    @Override
    public void onMenuLogoutSelected() {
        SharedPrefs.setUserName("");
        SharedPrefs.setUserId("");
        SharedPrefs.setIsLoggedIn(false);
        getView().showToast(makeToast(getAppContext().getString(R.string.logged_out_success)));
        Navigator.navigate(getActivityContext(), Navigator.SCREEN.LOGIN, true);
    }

    @Override
    public void takeOnline() {
        mModel.takeOnline();
    }

    @Override
    public void takeOffline() {
        mModel.takeOffline();
    }

    @Override
    public void getOnlineUsersCount() {
        mModel.getOnlineUsersCount();
    }

    @Override
    public void populateOnlineUsersRecyclerView(RecyclerView recyclerView, LifecycleOwner lifecycleOwner) {
        FirebaseRecyclerOptions<OnlineUsers> options =
                new FirebaseRecyclerOptions.Builder<OnlineUsers>()
                        .setQuery(onlineUsersQuery, OnlineUsers.class)
                        .setLifecycleOwner(lifecycleOwner)
                        .build();

        onlineUsersAdapter = new OnlineUsersAdapter(getActivityContext(), options);

        // Scroll to bottom on new messages
        onlineUsersAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                recyclerView.scrollToPosition(onlineUsersAdapter.getItemCount() - 1);
            }
        });
        recyclerView.setAdapter(onlineUsersAdapter);
    }

}
