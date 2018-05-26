package com.yakuzasqn.vdevoluntario.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.glide.slider.library.svg.GlideApp;
import com.yakuzasqn.vdevoluntario.R;
import com.yakuzasqn.vdevoluntario.model.User;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ParticipantListAdapter extends RecyclerView.Adapter<ParticipantListAdapter.ParticipantListViewHolder> {

    private Context context;
    private List<User> participantList;

    public ParticipantListAdapter(Context context, List<User> participantList) {
        this.context = context;
        this.participantList = participantList;
    }

    @NonNull
    @Override
    public ParticipantListAdapter.ParticipantListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_item_layout_participant, parent, false);

        return new ParticipantListAdapter.ParticipantListViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ParticipantListAdapter.ParticipantListViewHolder holder, int position) {
        User participant = participantList.get(position);

        GlideApp.with(context).load(participant.getPicture()).centerCrop().into(holder.photo);
        holder.name.setText(participant.getName());
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
        return participantList.size();
    }

    static class ParticipantListViewHolder extends RecyclerView.ViewHolder {
        CircleImageView photo;
        TextView name;

        ParticipantListViewHolder(View itemView){
            super(itemView);

            photo = itemView.findViewById(R.id.post_user_photo);
            name = itemView.findViewById(R.id.post_user_name);
        }
    }
}
