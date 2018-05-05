package com.yakuzasqn.vdevoluntario.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.glide.slider.library.svg.GlideApp;
import com.yakuzasqn.vdevoluntario.R;
import com.yakuzasqn.vdevoluntario.model.Post;
import com.yakuzasqn.vdevoluntario.model.User;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder>{
    private Context context;
    private List<Post> postList;

    public PostAdapter(Context context, List<Post> postList) {
        this.context = context;
        this.postList = postList;
    }

    @NonNull
    @Override
    public PostAdapter.PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_item_layout_post, parent, false);

        return new PostAdapter.PostViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PostAdapter.PostViewHolder holder, int position) {
        Post post = postList.get(position);
        User userFromPost = post.getUser();

        GlideApp.with(context).load(userFromPost.getPicture())
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .into(holder.userPhoto);
        holder.userName.setText(userFromPost.getName());
        // Post pode ser criado sem imagem
        if (post.getUrlImage() != null){
            GlideApp.with(context).load(post.getUrlImage())
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .into(holder.image);
        }
        holder.title.setText(post.getTitle());
        holder.description.setText(post.getDescription());
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

    static class PostViewHolder extends RecyclerView.ViewHolder {
        CircleImageView userPhoto;
        TextView userName, title, description;
        ImageView image;

        PostViewHolder(View itemView){
            super(itemView);

            userPhoto = itemView.findViewById(R.id.post_user_photo);
            userName = itemView.findViewById(R.id.post_user_name);
            title = itemView.findViewById(R.id.post_title);
            description = itemView.findViewById(R.id.post_description);
            image = itemView.findViewById(R.id.post_image);
        }
    }

}