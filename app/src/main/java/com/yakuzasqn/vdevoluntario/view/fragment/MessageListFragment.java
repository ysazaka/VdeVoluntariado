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
import com.yakuzasqn.vdevoluntario.model.Message;
import com.yakuzasqn.vdevoluntario.model.User;
import com.yakuzasqn.vdevoluntario.support.Constants;
import com.yakuzasqn.vdevoluntario.support.FirebaseUtils;
import com.yakuzasqn.vdevoluntario.support.RecyclerItemClickListener;
import com.yakuzasqn.vdevoluntario.util.Utils;
import com.yakuzasqn.vdevoluntario.view.activity.ChatActivity;

import org.apache.commons.lang3.StringEscapeUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MessageListFragment extends Fragment{

    private List<Message> msgList;
    public RecyclerView rv;
    private RecyclerView.OnItemTouchListener listener;

    private TextView tv_no_results;
    private ProgressBar progressBar;
    private RelativeLayout layout;

    private Boolean mIsLoading = false;
    private Boolean isGetMoreItens = true;

    private DatabaseReference mRef;
    private ValueEventListener valueEventListenerGroup;

    public MessageListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_message_list, container, false);

        rv = v.findViewById(R.id.rv_message_list);
        progressBar = v.findViewById(R.id.progress_message_list);
        tv_no_results = v.findViewById(R.id.tv_no_results);
        layout = v.findViewById(R.id.rl_message_list);

        setupRecycle();

        return v;
    }

    private void setupRecycle() {
        rv.setHasFixedSize(true);

        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        rv.setLayoutManager(mLayoutManager);

        loadMsgList();

        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (mIsLoading)
                    return;

                int visibleItemCount = mLayoutManager.getChildCount();
                int totalItemCount = mLayoutManager.getItemCount();
                int pastVisibleItems = mLayoutManager.findFirstVisibleItemPosition();
                if (pastVisibleItems + visibleItemCount >= totalItemCount) {
                    //End of list
                    if (isGetMoreItens) {
                        progressBar.setVisibility(View.VISIBLE);
                        ViewGroup.MarginLayoutParams marginLayoutParams =
                                (ViewGroup.MarginLayoutParams) rv.getLayoutParams();
                        marginLayoutParams.setMargins(0, 0, 0, 70);
                        rv.setLayoutParams(marginLayoutParams);

                        mIsLoading = true;
                        loadMsgList();
                    }
                }
            }
        });

        rv.computeVerticalScrollOffset();

        if (listener != null){
            rv.removeOnItemTouchListener(listener);
        }
        listener = new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                User chosenUser = msgList.get(position).getUserChatWith();
                Hawk.put(Constants.CHOSEN_POST_USER, chosenUser);

                Intent intent = new Intent(getActivity(), ChatActivity.class);
                startActivity(intent);
            }
        });

        rv.addOnItemTouchListener(listener);
    }

    private void loadMsgList(){
        final DisplayMetrics metrics = getResources().getDisplayMetrics();

        layout.removeView(progressBar);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, (int) (metrics.density * 30));
        lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        layout.addView(progressBar, lp);

        progressBar.setVisibility(View.INVISIBLE);
        ViewGroup.MarginLayoutParams marginLayoutParams =
                (ViewGroup.MarginLayoutParams) rv.getLayoutParams();
        marginLayoutParams.setMargins(0, 0, 0, 0);
        rv.setLayoutParams(marginLayoutParams);

        getDataFromFirebase();

    }

    private void getDataFromFirebase(){
        mRef = FirebaseUtils.getBaseRef().child("messages");
        msgList = new ArrayList<>();

        // Cria listener
        valueEventListenerGroup = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Limpar ArrayList de mensagens
                msgList.clear();

                // Recuperar mensagens
                for (DataSnapshot dados: dataSnapshot.getChildren()){
                    Message message = dados.getValue(Message.class);
                    msgList.add(message);
                }

                if (msgList.size() == 0){
                    rv.setVisibility(View.INVISIBLE);
                    tv_no_results.setVisibility(View.VISIBLE);
                } else {
                    rv.setVisibility(View.VISIBLE);
                    tv_no_results.setVisibility(View.INVISIBLE);

                    MessageListAdapter adapter = new MessageListAdapter(getContext(), msgList);
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
