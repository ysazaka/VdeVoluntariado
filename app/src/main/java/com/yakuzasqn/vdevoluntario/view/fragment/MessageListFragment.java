package com.yakuzasqn.vdevoluntario.view.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.orhanobut.hawk.Hawk;
import com.yakuzasqn.vdevoluntario.R;
import com.yakuzasqn.vdevoluntario.adapter.MessageListAdapter;
import com.yakuzasqn.vdevoluntario.model.Chat;
import com.yakuzasqn.vdevoluntario.model.Message;
import com.yakuzasqn.vdevoluntario.model.User;
import com.yakuzasqn.vdevoluntario.support.Constants;
import com.yakuzasqn.vdevoluntario.support.FirebaseUtils;
import com.yakuzasqn.vdevoluntario.support.RecyclerItemClickListener;
import com.yakuzasqn.vdevoluntario.util.Utils;
import com.yakuzasqn.vdevoluntario.view.activity.ChatActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MessageListFragment extends Fragment{

    private List<Chat> chatList;
    public RecyclerView rv;
    private RecyclerView.OnItemTouchListener listener;

    private TextView tv_no_results;

    private DatabaseReference mRef;
    private ValueEventListener valueEventListenerGroup;

    private User user;

    public MessageListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_message_list, container, false);

        rv = v.findViewById(R.id.rv_message_list);
        tv_no_results = v.findViewById(R.id.tv_no_results);

        user = Hawk.get(Constants.USER_SESSION);

        setupRecycle();
        getChatsFromFirebase();

        return v;
    }

    private void setupRecycle() {
        rv.setHasFixedSize(true);

        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        rv.setLayoutManager(mLayoutManager);

        if (listener != null){
            rv.removeOnItemTouchListener(listener);
        }
        listener = new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String chosenUserId = chatList.get(position).getUserId();
                Hawk.put(Constants.CHOSEN_CHAT_USER_ID, chosenUserId);

                Intent intent = new Intent(getActivity(), ChatActivity.class);
                startActivity(intent);
            }
        });

        rv.addOnItemTouchListener(listener);
    }

    private void getChatsFromFirebase(){
        mRef = FirebaseUtils.getBaseRef().child("chats").child(user.getId());
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

                    MessageListAdapter adapter = new MessageListAdapter(getContext(), chatList);
                    rv.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Utils.showToast(R.string.toast_failLoadingData, getActivity());
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
