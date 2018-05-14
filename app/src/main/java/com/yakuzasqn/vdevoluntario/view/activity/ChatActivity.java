package com.yakuzasqn.vdevoluntario.view.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.stfalcon.chatkit.messages.MessageInput;
import com.stfalcon.chatkit.messages.MessagesListAdapter;
import com.yakuzasqn.vdevoluntario.R;
import com.yakuzasqn.vdevoluntario.model.Message;

import java.util.List;

public class ChatActivity extends AppCompatActivity  implements
        MessageInput.InputListener,
        MessagesListAdapter.OnLoadMoreListener{

    private Message msg, message;
    private List<Message> msgList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Toolbar toolbar = findViewById(R.id.ca_toolbar);
        toolbar.setTitle("Nome do usuário");
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
        toolbar.setNavigationIcon(R.mipmap.ic_arrow_white);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    @Override
    public boolean onSubmit(CharSequence input) {
        return false;
    }

    @Override
    public void onLoadMore(int page, int totalItemsCount) {

    }
}
