package com.technologyleaks.retailistanchat.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.technologyleaks.retailistanchat.R;
import com.technologyleaks.retailistanchat.beans.Message;
import com.technologyleaks.retailistanchat.commons.CustomMethods;
import com.technologyleaks.retailistanchat.commons.SharedPrefs;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MessageAdapter extends FirebaseRecyclerAdapter<Message, MessageAdapter.MessageViewHolder>  {

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */


    private Context context;

    public MessageAdapter(@NonNull Context context, @NonNull FirebaseRecyclerOptions<Message> options) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull MessageViewHolder holder, int position, @NonNull Message model) {
        holder.bind(model);
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MessageViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_message, parent, false));
    }


    class MessageViewHolder extends RecyclerView.ViewHolder {
        private final String TAG = MessageViewHolder.class.getSimpleName();

        @BindView(R.id.textView_userName)
        TextView textView_userName;
        @BindView(R.id.textView_message)
        TextView textView_message;
        @BindView(R.id.textView_time)
        TextView textView_time;
        @BindView(R.id.layout_innerContainer)
        RelativeLayout layout_innerContainer;


        MessageViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(Message message) {
            String current_user = SharedPrefs.getUserId();

            if (message.getUserId().equals(current_user)) {
                layout_innerContainer.setGravity(Gravity.END);
                textView_message.setBackground(ContextCompat.getDrawable(context, R.drawable.chat_bubble_sent));
                textView_message.setTextColor(ContextCompat.getColor(context, R.color.colorLightText));
                textView_userName.setVisibility(View.GONE);

            } else {
                layout_innerContainer.setGravity(Gravity.START);
                textView_message.setBackground(ContextCompat.getDrawable(context, R.drawable.chat_bubble_received));
                textView_message.setTextColor(ContextCompat.getColor(context, android.R.color.white));
                textView_userName.setVisibility(View.VISIBLE);
            }


            textView_userName.setText(message.getSenderName());
            textView_userName.setText(message.getSenderName());
            textView_time.setText(CustomMethods.getTimeAgo(Long.valueOf(message.getTime())));
            textView_message.setText(message.getText());
        }
    }
}