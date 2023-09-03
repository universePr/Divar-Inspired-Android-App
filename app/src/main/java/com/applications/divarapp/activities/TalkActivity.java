package com.applications.divarapp.activities;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.applications.divarapp.R;
import com.applications.divarapp.adapters.MessageListAdapter;
import com.applications.divarapp.databinding.ActivityTalkBinding;
import com.applications.divarapp.models.ChatResponse;
import com.applications.divarapp.models.MessageModel;
import com.applications.divarapp.models.SaveChatDataModel;
import com.applications.divarapp.network.API;
import com.applications.divarapp.network.Endpoints;
import com.applications.divarapp.utils.Constants;
import com.applications.divarapp.utils.SPreferences;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TalkActivity extends AppCompatActivity {
    ActivityTalkBinding binding;
    //
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final Endpoints endpoints = API.getRetrofitInstance().create(Endpoints.class);

    private MessageListAdapter mMessageAdapter;
    private long ad_id;
    private String ad_title;
    private String collectinoName;
    private String role;
    private boolean newChat;
    private boolean saveChat = false;
    private String userPhone;

    private ProgressDialog progressDoalog;

    List<MessageModel> messageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTalkBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        //setContentView(R.layout.activity_talk);
        Intent i = getIntent();
        role = i.getStringExtra("role");
        if(role.equals("admin")){
            userPhone = i.getStringExtra("receiverPhone");
        }else{
            userPhone = SPreferences.getDefaults(Constants.Key_LoggedInUser_SP,getApplicationContext());
        }
        newChat = i.getBooleanExtra("newChat", true);
        ad_title = i.getStringExtra("ad_title");
        collectinoName = i.getStringExtra("ad_col");

        if(!newChat){
            saveChat = true;
            //getMessages();
            listenMessage();
        }else{
            ad_id = i.getLongExtra("ad_id", 0);
        }









        binding.textView.setText(getString(R.string.conversation_with) + " " + ad_title);

        messageList = new ArrayList<>();

        mMessageAdapter = new MessageListAdapter(this, messageList);
        binding.recyclerGchat.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerGchat.setAdapter(mMessageAdapter);


        progressDoalog = new ProgressDialog(this);
        progressDoalog.setMessage(getString(R.string.making_ad_chat));
        progressDoalog.setTitle(getString(R.string.chat_ad));
        progressDoalog.setCancelable(false);
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);


        binding.buttonSend.setOnClickListener(v -> {
                String msg = binding.editMessage.getText().toString();
                if(!msg.isEmpty() && !msg.trim().equals("")) {
                    Calendar cal = Calendar.getInstance();
                    Date date = new Date();
                    cal.setTime(date);
                    int year = cal.get(Calendar.YEAR);
                    int day = cal.get(Calendar.DAY_OF_MONTH);
                    int month = cal.get(Calendar.MONTH);
                    int hours = cal.get(Calendar.HOUR_OF_DAY);
                    int minute = cal.get(Calendar.MINUTE);
                    Map<String, Object> data = new HashMap<>();

                    data.put("phone", userPhone);
                    data.put("role", role);
                    data.put("msg", msg);
                    data.put("time", new Date());

                    if(newChat) {
                        // show it
                        progressDoalog.show();
                        createDocumentAndSendMessageToCollectionToFirestore(collectinoName, data);
                    }else {
                        sendMessageInCollectionToFirestore(data);
                    }
                }
        });
    }

    private void createDocumentAndSendMessageToCollectionToFirestore(String collectionName, Map<String,Object> data) {

        db.collection(collectionName).add(data)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        newChat = false;
                        saveChatToDb();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDoalog.dismiss();
                        Snackbar.make(binding.content, getString(R.string.there_is_a_problem_creating_the_ad_chat), Snackbar.LENGTH_INDEFINITE)
                                .setAction(getString(R.string.try_again), v -> {
                                    createDocumentAndSendMessageToCollectionToFirestore(collectionName, data);
                                })
                                .show();
                    }
                });
    }
    private void saveChatToDb(){

        endpoints.SaveUserChat(new SaveChatDataModel(ad_id, userPhone)).enqueue(new Callback<ChatResponse>() {
            @Override
            public void onResponse(Call<ChatResponse> call, Response<ChatResponse> response) {
                if (response.body().isResponse()) {
                    progressDoalog.dismiss();
                    saveChat = true;
                    Snackbar.make(binding.content, getString(R.string.the_ad_chat_was_created_successfully), Snackbar.LENGTH_SHORT)
                            .show();
                    binding.editMessage.setText("");
                    listenMessage();
                }
                else {
                    progressDoalog.dismiss();
                    Snackbar.make(binding.content, getString(R.string.there_is_a_problem_creating_the_ad_chat), Snackbar.LENGTH_INDEFINITE)
                            .setAction(getString(R.string.try_again), v -> {
                                saveChatToDb();
                            })
                            .show();
                }
            }

            @Override
            public void onFailure(Call<ChatResponse> call, Throwable t) {
                saveChat = false;
                Snackbar.make(binding.content, getString(R.string.there_is_a_problem_creating_the_ad_chat), Snackbar.LENGTH_INDEFINITE)
                        .setAction(getString(R.string.try_again), v -> {
                            saveChatToDb();
                        })
                        .show();
            }
        });
    }
    private void getMessages(){
        db.collection(collectinoName)
                .whereEqualTo("phone", userPhone)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int count = messageList.size();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                MessageModel model = new MessageModel();
                                model.setRole(document.getString("role"));
                                model.setTime(getReadableDataTime(document.getDate("time")));
                                model.setMessage(document.getString("msg"));
                                model.setPhone(document.getString("phone"));
                                model.dateObject = document.getDate("time");
                                if(role.equals("customer")){
                                    model.setNickName(getString(R.string.ad_customer));
                                }
                                messageList.add(model);
                                Toast.makeText(TalkActivity.this, model.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                            Collections.sort(messageList, (obj1, obj2) -> obj1.dateObject.compareTo(obj2.dateObject));
                            if(count == 0){
                                mMessageAdapter.notifyDataSetChanged();
                            }else{
                                mMessageAdapter.notifyItemRangeInserted(messageList.size(), messageList.size());
                                binding.recyclerGchat.smoothScrollToPosition(messageList.size() - 1);
                            }
                            binding.recyclerGchat.setVisibility(View.VISIBLE);

                        } else {

                        }
                    }
                });
    }

    private void listenMessage(){
        db.collection(collectinoName)
                .whereEqualTo("phone", userPhone)
                .whereEqualTo("role","customer")
                .addSnapshotListener(eventListener);
        db.collection(collectinoName)
                .whereEqualTo("phone", userPhone)
                .whereEqualTo("role", "admin")
                .addSnapshotListener(eventListener);
    }

    private final EventListener<QuerySnapshot> eventListener = ((value, error) -> {
       if(error != null){
           return;
       }
       if(value != null){
           int count = messageList.size();
           for(DocumentChange documentChange : value.getDocumentChanges()) {
               if(documentChange.getType() == DocumentChange.Type.ADDED) {
                   MessageModel model = new MessageModel();
                   model.setRole(documentChange.getDocument().getString("role"));
                   model.setTime(getReadableDataTime(documentChange.getDocument().getDate("time")));
                   model.setMessage(documentChange.getDocument().getString("msg"));
                   model.setPhone(documentChange.getDocument().getString("phone"));
                   model.dateObject = documentChange.getDocument().getDate("time");
                   if(role.equals("customer")){
                       model.setNickName(getString(R.string.ad_client));
                   }
                   messageList.add(model);
               }
           }
           Collections.sort(messageList, (obj1, obj2) -> obj1.dateObject.compareTo(obj2.dateObject));
           if(count == 0){
               mMessageAdapter.notifyDataSetChanged();
           }else{
               mMessageAdapter.notifyItemRangeInserted(messageList.size(), messageList.size());
               binding.recyclerGchat.smoothScrollToPosition(messageList.size() - 1);
           }
       }
    });

    private String getReadableDataTime(Date date){
        return new SimpleDateFormat("MMMM dd, yyyy - hh:mm a", Locale.getDefault()).format(date);
    }
    private void sendMessageInCollectionToFirestore(Map<String, Object> data) {
        db.collection(collectinoName).add(data);
        binding.editMessage.setText("");
    }
}