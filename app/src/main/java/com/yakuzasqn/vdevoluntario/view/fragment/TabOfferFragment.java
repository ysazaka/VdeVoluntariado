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
import com.yakuzasqn.vdevoluntario.model.Contact;
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
public class TabOfferFragment extends Fragment {

    private List<Post> postList;
    private PostAdapter adapter;
    private RecyclerView.OnItemTouchListener listener;

    private User actualUser;

    private DatabaseReference mRef;
    private ValueEventListener valueEventListenerGroup;

    public TabOfferFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_tab_offer, container, false);

        actualUser = Hawk.get(Constants.USER_SESSION);

        /***************************************************************
         Montagem do RecyclerView e do Adapter
         ****************************************************************/

        postList = new ArrayList<>();

        final RecyclerView rvOffer = v.findViewById(R.id.rv_offer);
        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());

        rvOffer.setLayoutManager(mLayoutManager);

        if (listener != null){
            rvOffer.removeOnItemTouchListener(listener);
        }
        listener = new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Post chosenPost = postList.get(position);
                User chosenPostUser = chosenPost.getUser();
//                Contact contact = new Contact(chosenPostUser.getId(), chosenPostUser.getName(), chosenPostUser.getEmail());

                if (!chosenPostUser.getId().equals(actualUser.getId())){
//                    createContactDatabase(contact);

                    Intent intent = new Intent(getActivity(), ChatActivity.class);
                    Hawk.put(Constants.CHOSEN_USER_FOR_CHAT, chosenPostUser);

                    startActivity(intent);
                }
            }
        });

        rvOffer.addOnItemTouchListener(listener);

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
                rvOffer.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Utils.showToast(R.string.toast_failLoadingData, getActivity());
            }
        };

        mRef.addValueEventListener(valueEventListenerGroup);

        return v;
    }

    private void createContactDatabase(Contact contact){
        try{
            DatabaseReference mRef = FirebaseUtils.getBaseRef().child("contacts");

            mRef.child(actualUser.getId()).child(contact.getId()).setValue(contact);

        } catch(Exception e){
            Utils.showToast(R.string.toast_errorCreatingContact, getActivity());
            e.printStackTrace();
        }
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
