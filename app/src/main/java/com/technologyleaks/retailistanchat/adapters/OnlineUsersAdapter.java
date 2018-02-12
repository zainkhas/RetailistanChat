package com.technologyleaks.retailistanchat.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.technologyleaks.retailistanchat.R;
import com.technologyleaks.retailistanchat.beans.OnlineUsers;
import com.technologyleaks.retailistanchat.commons.CustomMethods;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OnlineUsersAdapter extends FirebaseRecyclerAdapter<OnlineUsers, OnlineUsersAdapter.OnlineUsersViewHolder> {

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */


    private Context context;

    public OnlineUsersAdapter(@NonNull Context context, @NonNull FirebaseRecyclerOptions<OnlineUsers> options) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull OnlineUsersViewHolder holder, int position, @NonNull OnlineUsers model) {
        holder.bind(model);
    }

    @Override
    public OnlineUsersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new OnlineUsersViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_online_users, parent, false));
    }


    class OnlineUsersViewHolder extends RecyclerView.ViewHolder {
        private final String TAG = OnlineUsersViewHolder.class.getSimpleName();

        @BindView(R.id.textView_userName)
        TextView textView_userName;
        @BindView(R.id.textView_lastActive)
        TextView textView_lastActive;


        OnlineUsersViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(OnlineUsers onlineUsers) {
            textView_userName.setText(onlineUsers.getUsername());
            long last_active = Long.parseLong(onlineUsers.getLast_update_time());
            textView_lastActive.setText(CustomMethods.getLastActivityTimeAgo(last_active));
        }
    }
}