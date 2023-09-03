package com.applications.divarapp.fragments;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.applications.divarapp.activities.TalkActivity;
import com.applications.divarapp.adapters.ChatListAdapter;
import com.applications.divarapp.databinding.FragmentChatBinding;
import com.applications.divarapp.models.ChatModelResponse;
import com.applications.divarapp.network.API;
import com.applications.divarapp.network.Endpoints;
import com.applications.divarapp.utils.Constants;
import com.applications.divarapp.utils.SPreferences;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ChatFragment extends Fragment {

    private ChatListAdapter chatListAdapter;
    private List<ChatModelResponse> chatModelResponseList;
    private FragmentChatBinding binding;
    private final Endpoints endpoints = API.getRetrofitInstance().create(Endpoints.class);

    public static ChatFragment newInstance() {
        ChatFragment fragment = new ChatFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentChatBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String phone = SPreferences.getDefaults(Constants.Key_LoggedInUser_SP,getContext());
        chatModelResponseList = new ArrayList<>();
        chatListAdapter = new ChatListAdapter(getContext(), chatModelResponseList, phone,item -> {
            goToChat(item, phone);
        });
        binding.chatsRecycle.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.chatsRecycle.setAdapter(chatListAdapter);
        loadChats(phone);
    }

    private void loadChats(String phone){

        endpoints.GetMyChats(phone).enqueue(new Callback<List<ChatModelResponse>>() {
            @Override
            public void onResponse(Call<List<ChatModelResponse>> call, Response<List<ChatModelResponse>> response) {
                if(response.body() != null){
                    if(response.body().size() > 0) {
                        binding.helpText.setVisibility(View.GONE);
                        chatModelResponseList.clear();
                        chatModelResponseList = response.body();
                        chatListAdapter = new ChatListAdapter(getContext(), chatModelResponseList, phone, item -> {
                            goToChat(item, phone);
                        });
                        binding.chatsRecycle.setAdapter(chatListAdapter);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<ChatModelResponse>> call, Throwable t) {

            }
        });
    }
    private void goToChat(ChatModelResponse item,String phone){
        if(item.getPhone().equals(phone)){
            //Customer
            Intent i = new Intent(getActivity(), TalkActivity.class);
            i.putExtra("role", "customer");
            i.putExtra("newChat", false);
            i.putExtra("ad_title", item.getAdTitle());
            i.putExtra("ad_col", item.getCollectionName());
            startActivity(i);
        }else{
            //Admin
            Intent i = new Intent(getActivity(), TalkActivity.class);
            i.putExtra("role", "admin");
            i.putExtra("newChat", false);
            i.putExtra("ad_title", item.getAdTitle());
            i.putExtra("ad_col", item.getCollectionName());
            i.putExtra("receiverPhone", item.getPhone());

            startActivity(i);
        }
    }
}