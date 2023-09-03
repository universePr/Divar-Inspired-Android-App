package com.applications.divarapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.applications.divarapp.R;
import com.applications.divarapp.models.ChatModelResponse;

import java.util.ArrayList;
import java.util.List;

public class ChatListAdapter extends RecyclerView.Adapter {

    public interface OnItemClickListener {
        void onItemClick(ChatModelResponse item);
    }

    private final Context mContext;
    private final String currentPhoneNumber;
    private List<ChatModelResponse> mMessageList = new ArrayList<>();
    //Click listener
    private final ChatListAdapter.OnItemClickListener listener;

    public ChatListAdapter(Context context, List<ChatModelResponse> messageList, String currentPhoneNumber, OnItemClickListener listener) {
        mContext = context;
        mMessageList = messageList;
        this.currentPhoneNumber = currentPhoneNumber;
        this.listener = listener;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.my_chat_item_layout, parent, false);
            return new ChatHolder(view);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ChatModelResponse message = (ChatModelResponse) mMessageList.get(position);
        ((ChatHolder) holder).bind(message, listener);

    }


    @Override
    public int getItemCount() {
        return mMessageList.size();
    }


    private class ChatHolder extends RecyclerView.ViewHolder {
        TextView ad_title, chat_creator;

        ChatHolder(View itemView) {
            super(itemView);

            ad_title = (TextView) itemView.findViewById(R.id.ad_title);
            chat_creator = (TextView) itemView.findViewById(R.id.chat_creator);
        }

        void bind(ChatModelResponse chat, final ChatListAdapter.OnItemClickListener listener) {
            ad_title.setText(chat.getAdTitle());
            if(currentPhoneNumber.equals(chat.getPhone())) {
                chat_creator.setText(mContext.getResources().getString(R.string.you_are_an_ad_customer));
            }else{
                chat_creator.setText(mContext.getResources().getString(R.string.you_are_owner_of_the_ad));
            }
            itemView.setOnClickListener(v -> {
                listener.onItemClick(chat);
            });
        }
    }
}
