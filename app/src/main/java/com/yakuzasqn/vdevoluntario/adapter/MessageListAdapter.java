package com.yakuzasqn.vdevoluntario.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.stfalcon.chatkit.utils.DateFormatter;
import com.yakuzasqn.vdevoluntario.R;
import com.yakuzasqn.vdevoluntario.model.Message;
import com.yakuzasqn.vdevoluntario.model.User;

import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageListAdapter extends RecyclerView.Adapter<MessageListAdapter.MessageListViewHolder>{
    private Context context;
    private List<Message> listMsg;

    public MessageListAdapter(Context context, List<Message> listMsg) {
        this.context = context;
        this.listMsg = listMsg;
    }

    @Override
    public MessageListAdapter.MessageListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_item_layout_post, parent, false);

        return new MessageListAdapter.MessageListViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageListAdapter.MessageListViewHolder holder, int position) {
        final User user = listMsg.get(position).getUserChatWith();
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
        return listMsg.size();
    }

    static class MessageListViewHolder extends RecyclerView.ViewHolder {
        LinearLayout llMessage;
        CircleImageView foto;
        TextView nome, descricao, hora;
        ImageView like;

        MessageListViewHolder(View itemView){
            super(itemView);


        }
    }

    private String getDateString(Date date) {
        return DateFormatter.format(date, DateFormatter.Template.TIME);
    }
}
