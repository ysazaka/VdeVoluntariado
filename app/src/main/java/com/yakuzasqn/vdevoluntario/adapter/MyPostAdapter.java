package com.yakuzasqn.vdevoluntario.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.glide.slider.library.svg.GlideApp;
import com.google.firebase.database.DatabaseReference;
import com.orhanobut.hawk.Hawk;
import com.yakuzasqn.vdevoluntario.R;
import com.yakuzasqn.vdevoluntario.model.Group;
import com.yakuzasqn.vdevoluntario.model.Post;
import com.yakuzasqn.vdevoluntario.model.User;
import com.yakuzasqn.vdevoluntario.support.Constants;
import com.yakuzasqn.vdevoluntario.support.FirebaseUtils;
import com.yakuzasqn.vdevoluntario.view.activity.ManagePostActivity;
import com.yakuzasqn.vdevoluntario.view.activity.UpdatePostActivity;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyPostAdapter extends RecyclerView.Adapter<MyPostAdapter.MyPostViewHolder>{
    private Context context;
    private List<Post> postList;

    public MyPostAdapter(Context context, List<Post> postList) {
        this.context = context;
        this.postList = postList;
    }

    @NonNull
    @Override
    public MyPostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_item_layout_my_post, parent, false);

        return new MyPostAdapter.MyPostViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyPostViewHolder holder, final int position) {
        final Post post = postList.get(position);

        holder.name.setText(post.getTitle());
        // Post pode ser criado sem imagem
        if (post.getUrlImage() != null){
            GlideApp.with(context).load(post.getUrlImage())
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .into(holder.photo);
        }

        holder.llMyPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Hawk.put(Constants.CHOSEN_POST, post);

                PopupMenu popupMenu = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                    popupMenu = new PopupMenu(context, holder.llMyPost, Gravity.END);
                } else {
                    popupMenu = new PopupMenu(context, holder.llMyPost);
                }
                popupMenu.inflate(R.menu.selected_post);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.itemUpdate:
                                if (post.getGroup() == null)
                                    Hawk.delete(Constants.CHOSEN_GROUP);
                                Intent intent = new Intent(context, UpdatePostActivity.class);
                                ((Activity) context).startActivityForResult(intent, Constants.REQUEST_CODE_POST_SUCCESS);
                                return true;
                            case R.id.itemDelete:
                                DatabaseReference mRef = FirebaseUtils.getBaseRef().child("posts");
                                mRef.child(postList.get(position).getId()).setValue(null);
                                Toast.makeText(context, "Excluído com sucesso!", Toast.LENGTH_SHORT).show();
                                ((Activity)context).finish();
                                context.startActivity(new Intent(context, ManagePostActivity.class));
                                return true;
                        }
                        return false;
                    }
                });

                popupMenu.show();
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    static class MyPostViewHolder extends RecyclerView.ViewHolder{
        LinearLayout llMyPost;
        CircleImageView photo;
        TextView name;

        MyPostViewHolder(View itemView){
            super(itemView);
            llMyPost = itemView.findViewById(R.id.ll_my_post);
            photo = itemView.findViewById(R.id.post_civ_photo);
            name = itemView.findViewById(R.id.post_tv_name);
        }
    }
}
