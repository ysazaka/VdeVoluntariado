package com.yakuzasqn.vdevoluntario.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.orhanobut.hawk.Hawk;
import com.yakuzasqn.vdevoluntario.R;
import com.yakuzasqn.vdevoluntario.adapter.PostAdapter;
import com.yakuzasqn.vdevoluntario.model.Post;
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
public class TabContributeFragment extends Fragment {

    private List<Post> postList;
    private PostAdapter adapter;
    private RecyclerView.OnItemTouchListener listener;

    private User actualUser;

    private DatabaseReference mRef;
    private ValueEventListener valueEventListenerGroup;

    public TabContributeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_tab_contribute, container, false);

        actualUser = Hawk.get(Constants.USER_SESSION);

        /***************************************************************
         Montagem do RecyclerView e do Adapter
         ****************************************************************/

        postList = new ArrayList<>();

        final RecyclerView rvContribute = v.findViewById(R.id.rv_contribute);
        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());

        rvContribute.setLayoutManager(mLayoutManager);

        if (listener != null){
            rvContribute.removeOnItemTouchListener(listener);
        }
        listener = new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Post chosenPost = postList.get(position);
                User chosenPostUser = chosenPost.getUser();

                if (chosenPostUser != actualUser){
                    Intent intent = new Intent(getActivity(), ChatActivity.class);
                    Hawk.put(Constants.CHOSEN_POST_USER, chosenPostUser);

                    startActivity(intent);
                }
            }
        });

//        rvContribute.addOnItemTouchListener(listener);

        /***************************************************************
         Recuperar dados do Firebase
         ****************************************************************/

        mRef = FirebaseUtils.getBaseRef().child("posts");
        // Cria listener
        valueEventListenerGroup = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Limpar ArrayList de posts
                postList.clear();

                // Recuperar posts
                for (DataSnapshot dados: dataSnapshot.getChildren()){
                    Post post = dados.getValue(Post.class);
                    postList.add(post);
                }

                adapter = new PostAdapter(getContext(), postList);
                adapter.notifyDataSetChanged();
                rvContribute.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Utils.showToast(R.string.toast_failLoadingData, getActivity());
            }
        };

        mRef.addValueEventListener(valueEventListenerGroup);

        return v;
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
