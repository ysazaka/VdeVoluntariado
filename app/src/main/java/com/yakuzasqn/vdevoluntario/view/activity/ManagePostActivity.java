package com.yakuzasqn.vdevoluntario.view.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.orhanobut.hawk.Hawk;
import com.yakuzasqn.vdevoluntario.R;
import com.yakuzasqn.vdevoluntario.adapter.InstituteAdapter;
import com.yakuzasqn.vdevoluntario.adapter.MyPostAdapter;
import com.yakuzasqn.vdevoluntario.adapter.PostAdapter;
import com.yakuzasqn.vdevoluntario.model.Group;
import com.yakuzasqn.vdevoluntario.model.Post;
import com.yakuzasqn.vdevoluntario.model.User;
import com.yakuzasqn.vdevoluntario.support.Constants;
import com.yakuzasqn.vdevoluntario.support.FirebaseUtils;
import com.yakuzasqn.vdevoluntario.support.RecyclerItemClickListener;
import com.yakuzasqn.vdevoluntario.util.Utils;

import java.util.ArrayList;
import java.util.List;

public class ManagePostActivity extends AppCompatActivity {

    private List<Post> postList;
    private RecyclerView.OnItemTouchListener listener;
    private MyPostAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_post);

        Utils.setBackableToolbar(R.id.mpo_toolbar, "Gerenciar ofertas", ManagePostActivity.this);

        LinearLayout llNewPost = findViewById(R.id.ll_new_post);
        llNewPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ManagePostActivity.this, CreatePostActivity.class);
                intent.putExtra("typeOfPost", Constants.OFFER);
                startActivity(intent);
            }
        });

        /***************************************************************
         Montagem do RecyclerView e do Adapter
         ****************************************************************/

        postList = new ArrayList<>();

        final RecyclerView rvMyPost = findViewById(R.id.rv_my_post);
        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(ManagePostActivity.this);

        rvMyPost.setLayoutManager(mLayoutManager);

        if (listener != null){
            rvMyPost.removeOnItemTouchListener(listener);
        }
        listener = new RecyclerItemClickListener(ManagePostActivity.this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }
        });

        rvMyPost.addOnItemTouchListener(listener);

        /***************************************************************
         Recuperar dados do Firebase
         ****************************************************************/
        User user = Hawk.get(Constants.USER_SESSION);

        ProgressDialog dialog = ProgressDialog.show(ManagePostActivity.this, "", "Carregando grupos, aguarde...", true);
        DatabaseReference mRef = FirebaseUtils.getBaseRef().child("posts");
        Query queryRef = mRef.orderByChild("creatorId").equalTo(user.getId());
        queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                postList.clear();

                for (DataSnapshot dados: dataSnapshot.getChildren()){
                    Post post = dados.getValue(Post.class);
                    postList.add(post);
                }

                adapter = new MyPostAdapter(ManagePostActivity.this, postList);
                rvMyPost.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Utils.showToast(R.string.toast_failLoadingData, ManagePostActivity.this);
            }
        });

        dialog.dismiss();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
