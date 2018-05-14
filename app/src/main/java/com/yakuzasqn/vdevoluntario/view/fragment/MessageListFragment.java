package com.yakuzasqn.vdevoluntario.view.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.manager.SupportRequestManagerFragment;
import com.glide.slider.library.svg.GlideApp;
import com.stfalcon.chatkit.commons.ImageLoader;
import com.stfalcon.chatkit.dialogs.DialogsList;
import com.stfalcon.chatkit.dialogs.DialogsListAdapter;
import com.yakuzasqn.vdevoluntario.R;
import com.yakuzasqn.vdevoluntario.model.Dialog;

/**
 * A simple {@link Fragment} subclass.
 */
public class MessageListFragment extends Fragment implements DialogsListAdapter.OnDialogClickListener<Dialog> {

    private DialogsList dialogs;

    public MessageListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_message_list, container, false);

        dialogs = v.findViewById(R.id.dialogsList);

        DialogsListAdapter dialogsListAdapter = new DialogsListAdapter<>(new ImageLoader() {
            @Override
            public void loadImage(ImageView imageView, String url) {
                //If you using another library - write here your way to load image
                GlideApp.with(getActivity().getApplicationContext()).load(url).into(imageView);
            }
        });

        dialogs.setAdapter(dialogsListAdapter);

        return v;
    }

    @Override
    public void onDialogClick(Dialog dialog) {

    }
}
