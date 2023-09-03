package com.applications.divarapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.applications.divarapp.R;
import com.applications.divarapp.activities.LoginActivity;
import com.applications.divarapp.activities.MainActivity;

public class LockChatFragment extends Fragment {

    public static LockChatFragment newInstance() {
        LockChatFragment fragment = new LockChatFragment();
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
        return inflater.inflate(R.layout.fragment_lock_chat, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((Button)view.findViewById(R.id.btn_login)).setOnClickListener(v -> {
            Intent i = new Intent(getActivity(), LoginActivity.class);
            startActivity(i);
        });
    }
}