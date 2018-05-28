package com.yakuzasqn.vdevoluntario.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.glide.slider.library.svg.GlideApp;
import com.stfalcon.chatkit.utils.DateFormatter;
import com.yakuzasqn.vdevoluntario.R;
import com.yakuzasqn.vdevoluntario.model.Chat;
import com.yakuzasqn.vdevoluntario.model.Group;
import com.yakuzasqn.vdevoluntario.model.User;

import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageListAdapter extends RecyclerView.Adapter<MessageListAdapter.MessageListViewHolder>{
    private Context context;
    private List<Chat> chatList;

    public MessageListAdapter(Context context, List<Chat> chatList) {
        this.context = context;
        this.chatList = chatList;
    }

    @Override
    public MessageListAdapter.MessageListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_item_layout_message, parent, false);

        return new MessageListAdapter.MessageListViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageListAdapter.MessageListViewHolder holder, int position) {
        Chat chat = chatList.get(position);

        Date date = chatList.get(position).getCreatedAt();
        String lastMessageTime = getDateString(date);

        if (chat != null){
            User user = chat.getChosenUser();
            Group group = chat.getChosenGroup();

            if (user != null){
                GlideApp.with(context).load(user.getPicture())
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                        .into(holder.photo);

                holder.name.setText(user.getName());
            } else if (group != null){
                GlideApp.with(context).load(group.getPicture())
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                        .into(holder.photo);

                holder.name.setText(group.getName());
            }

        }
        holder.message.setText(chat.getMessage());
        holder.time.setText(lastMessageTime);
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
        return chatList.size();
    }

    static class MessageListViewHolder extends RecyclerView.ViewHolder {
        CircleImageView photo;
        TextView name, message, time;

        MessageListViewHolder(View itemView){
            super(itemView);

            photo = itemView.findViewById(R.id.message_photo);
            name = itemView.findViewById(R.id.message_user_name);
            message = itemView.findViewById(R.id.message_last_one);
            time = itemView.findViewById(R.id.message_time);
        }
    }

    private String getDateString(Date date) {
        return DateFormatter.format(date, DateFormatter.Template.TIME);
    }
}
