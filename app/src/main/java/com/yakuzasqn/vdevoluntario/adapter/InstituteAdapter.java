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
import com.yakuzasqn.vdevoluntario.R;
import com.yakuzasqn.vdevoluntario.model.Group;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class InstituteAdapter extends RecyclerView.Adapter<InstituteAdapter.InstituteViewHolder>{

    private Context context;
    private List<Group> instituteList;

    public InstituteAdapter(Context context, List<Group> instituteList) {
        this.context = context;
        this.instituteList = instituteList;
    }

    @NonNull
    @Override
    public InstituteAdapter.InstituteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_item_layout_institute, parent, false);

        return new InstituteAdapter.InstituteViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull InstituteAdapter.InstituteViewHolder holder, int position) {
        final Group group = instituteList.get(position);

        GlideApp.with(context).load(group.getPicture())
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .into(holder.civ);
        holder.tvName.setText(group.getName());
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
        return instituteList.size();
    }

    public static class InstituteViewHolder extends RecyclerView.ViewHolder {
        CircleImageView civ;
        TextView tvName;

        public InstituteViewHolder(View itemView){
            super(itemView);
            civ = itemView.findViewById(R.id.institute_photo);
            tvName = itemView.findViewById(R.id.institute_name);
        }
    }
}
