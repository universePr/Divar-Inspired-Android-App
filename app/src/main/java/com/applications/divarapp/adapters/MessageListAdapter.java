package com.applications.divarapp.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.applications.divarapp.R;
import com.applications.divarapp.models.MessageModel;

import java.util.ArrayList;
import java.util.List;

public class MessageListAdapter extends RecyclerView.Adapter {
    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;

    private final Context mContext;
    private List<MessageModel> mMessageList = new ArrayList<>();

    public MessageListAdapter(Context context, List<MessageModel> messageList) {
        mContext = context;
        mMessageList = messageList;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

        if (viewType == VIEW_TYPE_MESSAGE_SENT) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_msg_me, parent, false);
            return new SentMessageHolder(view);
        } else if (viewType == VIEW_TYPE_MESSAGE_RECEIVED) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_msg_other, parent, false);
            return new ReceivedMessageHolder(view);
        }

        return null;
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MessageModel message = (MessageModel) mMessageList.get(position);

        switch (holder.getItemViewType()) {
            case VIEW_TYPE_MESSAGE_SENT:
                ((SentMessageHolder) holder).bind(message);
                break;
            case VIEW_TYPE_MESSAGE_RECEIVED:
                ((ReceivedMessageHolder) holder).bind(message);
        }
    }
    // Determines the appropriate ViewType according to the sender of the message.
    @Override
    public int getItemViewType(int position) {
        MessageModel message = (MessageModel) mMessageList.get(position);

        if (message.getRole().equals("admin")) {
            // If the current user is the sender of the message
            return VIEW_TYPE_MESSAGE_SENT;
        } else {
            // If some other user sent the message
            return VIEW_TYPE_MESSAGE_RECEIVED;
        }
    }
    @Override
    public int getItemCount() {
        return mMessageList.size();
    }
    @SuppressLint("NotifyDataSetChanged")
    public void updateList (List<MessageModel> items) {
        if (items != null && items.size() > 0) {
            mMessageList.clear();
            mMessageList.addAll(items);
            this.notifyDataSetChanged();

        }
    }
    private class ReceivedMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText, timeText, nameText;

        ReceivedMessageHolder(View itemView) {
            super(itemView);
            messageText = (TextView) itemView.findViewById(R.id.text_message_other);
            timeText = (TextView) itemView.findViewById(R.id.text_timestamp_other);
            nameText = (TextView) itemView.findViewById(R.id.text_user_other);
        }

        void bind(MessageModel message) {
            messageText.setText(message.getMessage());

            // Format the stored timestamp into a readable String using method.
            timeText.setText(message.getTime());
            nameText.setText(message.getNickName());


        }
    }
    private class SentMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText, timeText;

        SentMessageHolder(View itemView) {
            super(itemView);

            messageText = (TextView) itemView.findViewById(R.id.text_message_me);
            timeText = (TextView) itemView.findViewById(R.id.text_timestamp_me);
        }

        void bind(MessageModel message) {
            messageText.setText(message.getMessage());

            // Format the stored timestamp into a readable String using method.
            timeText.setText(message.getTime());
        }
    }
}
