package com.yakuzasqn.vdevoluntario.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.orhanobut.hawk.Hawk;
import com.yakuzasqn.vdevoluntario.R;
import com.yakuzasqn.vdevoluntario.adapter.PostAdapter;
import com.yakuzasqn.vdevoluntario.model.Group;
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
    private TextView tvNotFound;

    private User actualUser;

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

        tvNotFound = v.findViewById(R.id.tv_not_found_o);
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
                Group chosenPostGroup = chosenPost.getGroup();

                if (chosenPostUser != null && !chosenPostUser.getId().equals(actualUser.getId())){
                    Intent intent = new Intent(getActivity(), ChatActivity.class);
                    Hawk.put(Constants.CHOSEN_USER_FOR_CHAT, chosenPostUser);
                    Hawk.delete(Constants.CHOSEN_GROUP_FOR_CHAT);
                    Hawk.delete(Constants.CHOSEN_GROUP);

                    startActivity(intent);
                } else if (chosenPostGroup != null){
                    Intent intent = new Intent(getActivity(), ChatActivity.class);
                    Hawk.put(Constants.CHOSEN_GROUP_FOR_CHAT, chosenPostGroup);
                    Hawk.delete(Constants.CHOSEN_USER_FOR_CHAT);
                    Hawk.delete(Constants.CHOSEN_GROUP);

                    startActivity(intent);
                } else {
                    Utils.showToast(R.string.toast_selfpost, getActivity());
                }
            }
        });

        rvOffer.addOnItemTouchListener(listener);

        /***************************************************************
         Recuperar dados do Firebase
         ****************************************************************/

        DatabaseReference mRef = FirebaseUtils.getBaseRef().child("posts");
        Query queryRef = mRef.orderByChild("type").equalTo(Constants.OFFER);
        queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                postList.clear();

                for (DataSnapshot dados: dataSnapshot.getChildren()){
                    Post post = dados.getValue(Post.class);
                    postList.add(post);
                }

                adapter = new PostAdapter(getContext(), postList);
                rvOffer.setAdapter(adapter);
                adapter.notifyDataSetChanged();

                if (postList.size() > 0) {
                    rvOffer.setVisibility(View.VISIBLE);
                    tvNotFound.setVisibility(View.GONE);
                } else {
                    rvOffer.setVisibility(View.GONE);
                    tvNotFound.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Utils.showToast(R.string.toast_failLoadingData, getActivity());
            }
        });

        return v;
    }

}
