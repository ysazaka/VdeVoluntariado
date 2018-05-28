package com.yakuzasqn.vdevoluntario.view.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.orhanobut.hawk.Hawk;
import com.yakuzasqn.vdevoluntario.R;
import com.yakuzasqn.vdevoluntario.adapter.MessageListAdapter;
import com.yakuzasqn.vdevoluntario.model.Chat;
import com.yakuzasqn.vdevoluntario.model.Group;
import com.yakuzasqn.vdevoluntario.model.User;
import com.yakuzasqn.vdevoluntario.support.Constants;
import com.yakuzasqn.vdevoluntario.support.FirebaseUtils;
import com.yakuzasqn.vdevoluntario.support.RecyclerItemClickListener;
import com.yakuzasqn.vdevoluntario.util.Utils;

import java.util.ArrayList;
import java.util.List;

public class CheckChatActivity extends AppCompatActivity {

    private List<Chat> chatList;
    public RecyclerView rv;
    private RecyclerView.OnItemTouchListener listener;

    private TextView tv_no_results;

    private DatabaseReference mRef;
    private ValueEventListener valueEventListenerGroup;

    private Group group;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_chat);

        Utils.setBackableToolbar(R.id.checkchat_toolbar, "Conversas", CheckChatActivity.this);

        rv = findViewById(R.id.rv_chat_list);
        tv_no_results = findViewById(R.id.tv_no_results);

        group = Hawk.get(Constants.CHOSEN_GROUP);

        setupRecycle();
        getChatsFromFirebase();
    }

    // Corrigir comportamento da seta de voltar - Toolbar customizada
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupRecycle() {
        rv.setHasFixedSize(true);

        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(CheckChatActivity.this);
        rv.setLayoutManager(mLayoutManager);

        if (listener != null){
            rv.removeOnItemTouchListener(listener);
        }
        listener = new RecyclerItemClickListener(CheckChatActivity.this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                User chosenUser = chatList.get(position).getChosenUser();
                Group chosenGroup = chatList.get(position).getChosenGroup();
                if (chosenUser != null){
                    Hawk.put(Constants.CHOSEN_USER_FOR_CHAT, chosenUser);
                    Hawk.delete(Constants.CHOSEN_GROUP_FOR_CHAT);
                } else if (chosenGroup != null){
                    Hawk.put(Constants.CHOSEN_GROUP_FOR_CHAT, chosenGroup);
                    Hawk.delete(Constants.CHOSEN_USER_FOR_CHAT);
                }

                Intent intent = new Intent(CheckChatActivity.this, ChatActivity.class);
                startActivity(intent);
            }
        });

        rv.addOnItemTouchListener(listener);
    }

    private void getChatsFromFirebase(){
        mRef = FirebaseUtils.getBaseRef().child("chats").child(group.getId());
        chatList = new ArrayList<>();

        // Cria listener
        valueEventListenerGroup = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Limpar ArrayList de chats
                chatList.clear();

                // Recuperar chats
                for (DataSnapshot dados: dataSnapshot.getChildren()){
                    Chat chat = dados.getValue(Chat.class);
                    chatList.add(chat);
                }

                if (chatList.size() == 0){
                    rv.setVisibility(View.INVISIBLE);
                    tv_no_results.setVisibility(View.VISIBLE);
                } else {
                    rv.setVisibility(View.VISIBLE);
                    tv_no_results.setVisibility(View.INVISIBLE);

                    MessageListAdapter adapter = new MessageListAdapter(CheckChatActivity.this, chatList);
                    rv.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Utils.showToast(R.string.toast_failLoadingData, CheckChatActivity.this);
            }
        };

        mRef.addValueEventListener(valueEventListenerGroup);
    }

    @Override
    public void onStart() {
        super.onStart();
        mRef.addValueEventListener(valueEventListenerGroup);
    }

    @Override
    public void onStop() {
        super.onStop();
        mRef.removeEventListener(valueEventListenerGroup);
    }
}
