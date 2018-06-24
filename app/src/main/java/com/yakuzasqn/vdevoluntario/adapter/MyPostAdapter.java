package com.yakuzasqn.vdevoluntario.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.glide.slider.library.svg.GlideApp;
import com.yakuzasqn.vdevoluntario.R;
import com.yakuzasqn.vdevoluntario.model.Group;
import com.yakuzasqn.vdevoluntario.model.Post;
import com.yakuzasqn.vdevoluntario.model.User;

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
    public void onBindViewHolder(@NonNull final MyPostViewHolder holder, int position) {
        final Post post = postList.get(position);
        User userFromPost = post.getUser();
        Group groupFromPost = post.getGroup();

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
                post.setSelected(!post.isSelected());
                holder.llMyPost.setBackgroundColor(post.isSelected() ? context.getResources().getColor(R.color.colorPrimary)
                        : context.getResources().getColor(R.color.colorWhite));
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

    static class MyPostViewHolder extends RecyclerView.ViewHolder {
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
