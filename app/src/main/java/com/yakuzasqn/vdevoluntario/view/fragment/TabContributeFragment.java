package com.yakuzasqn.vdevoluntario.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.orhanobut.hawk.Hawk;
import com.yakuzasqn.vdevoluntario.R;
import com.yakuzasqn.vdevoluntario.adapter.PostAdapter;
import com.yakuzasqn.vdevoluntario.model.Post;
import com.yakuzasqn.vdevoluntario.model.User;
import com.yakuzasqn.vdevoluntario.support.Constants;
import com.yakuzasqn.vdevoluntario.support.RecyclerItemClickListener;
import com.yakuzasqn.vdevoluntario.view.activity.ChatActivity;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class TabContributeFragment extends Fragment {

    private Context context;
    private List<Post> postList;

    private RecyclerView.OnItemTouchListener listener;

    public TabContributeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_tab_contribute, container, false);

        RecyclerView rvContribute = v.findViewById(R.id.rv_contribute);

        PostAdapter postAdapter = new PostAdapter(context, postList);
        postAdapter.notifyDataSetChanged();
        rvContribute.setAdapter(postAdapter);

        if (listener != null){
            rvContribute.removeOnItemTouchListener(listener);
        }
        listener = new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Post chosenPost = postList.get(position);
                User chosenPostUser = chosenPost.getUser();

                Intent intent = new Intent(getActivity(), ChatActivity.class);
                Hawk.put(Constants.CHOSEN_POST_USER, chosenPostUser);

                startActivity(intent);
            }
        });

        rvContribute.addOnItemTouchListener(listener);

        return v;
    }

}
