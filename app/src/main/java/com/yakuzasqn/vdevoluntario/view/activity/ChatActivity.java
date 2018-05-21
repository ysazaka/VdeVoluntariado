package com.yakuzasqn.vdevoluntario.view.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.glide.slider.library.svg.GlideApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.orhanobut.hawk.Hawk;
import com.stfalcon.chatkit.commons.ImageLoader;
import com.stfalcon.chatkit.messages.MessageHolders;
import com.stfalcon.chatkit.messages.MessageInput;
import com.stfalcon.chatkit.messages.MessagesList;
import com.stfalcon.chatkit.messages.MessagesListAdapter;
import com.yakuzasqn.vdevoluntario.R;
import com.yakuzasqn.vdevoluntario.model.Chat;
import com.yakuzasqn.vdevoluntario.model.Message;
import com.yakuzasqn.vdevoluntario.model.User;
import com.yakuzasqn.vdevoluntario.support.Constants;
import com.yakuzasqn.vdevoluntario.support.FirebaseUtils;
import com.yakuzasqn.vdevoluntario.util.Utils;

import org.apache.commons.lang3.StringEscapeUtils;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity  implements
        MessageInput.InputListener,
        MessagesListAdapter.OnLoadMoreListener{

    private List<Message> msgList;

    // Destinatário = chosenUser | Remetente = user
    private User user, chosenUser;

    private Chat chat;

    private MessagesList messagesList;
    private MessagesListAdapter<Message> adapter;
    private ImageLoader imageLoader;

    private Boolean mIsLoading = false;
    private Boolean isGetMoreItens = true;
    private ProgressBar progressBar;

    private DatabaseReference mRef;
    private ValueEventListener valueEventListenerMensagem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        user = Hawk.get(Constants.USER_SESSION);
        chosenUser = Hawk.get(Constants.CHOSEN_POST_USER);

        if (chosenUser != null)
            Utils.setBackableToolbar(R.id.chat_toolbar, chosenUser.getName(), ChatActivity.this);
        else
            Utils.setBackableToolbar(R.id.chat_toolbar, "", ChatActivity.this);

        messagesList = findViewById(R.id.messagesList);
        setupMsgList();
        msgList = new ArrayList<>();

        imageLoader = new ImageLoader() {
            @Override
            public void loadImage(ImageView imageView, String url) {
                GlideApp.with(getApplicationContext()).load(url).into(imageView);
            }
        };

        MessageInput input = findViewById(R.id.input);
        input.setInputListener(this);

        loadMessages();
    }

    // Corrigir comportamento da seta de voltar - Toolbar customizada
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSubmit(CharSequence input) {
        String str = StringEscapeUtils.escapeJava(input.toString());
        Long timestampLong = System.currentTimeMillis()/1000;
        Message msg = new Message(user.getId(), str, timestampLong);
        adapter.addToStart(msg, true);

        createMessageDatabase(msg);

        // Save the chat to actual user
        chat = new Chat();
        chat.setUserId(user.getId());
        chat.setUserPhoto(user.getPicture());
        chat.setUserName(user.getName());
        chat.setMessage(msg.getText());
        chat.setTimestamp(msg.getTimestamp());
        saveChatDatabase(chat, user.getId(), chosenUser.getId());

        // Save the chat to destiny user
        chat = new Chat();
        chat.setUserId(chosenUser.getId());
        chat.setUserPhoto(chosenUser.getPicture());
        chat.setUserName(chosenUser.getName());
        chat.setMessage(msg.getText());
        chat.setTimestamp(msg.getTimestamp());
        saveChatDatabase(chat, chosenUser.getId(), user.getId());

        return true;
    }

    @Override
    public void onLoadMore(int page, int totalItemsCount) {
        // Method to use with pagination control
    }

    private void loadMessages(){
        mRef = FirebaseUtils.getBaseRef().child("messages").child(user.getId()).child(chosenUser.getId());
        // Cria listener
        valueEventListenerMensagem = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Limpar ArrayList de mensagens
                msgList.clear();

                // Recuperar mensagens
                // Get map of users in datasnapshot
//                collectMessages((Map<String,Object>) dataSnapshot.getValue());
                for (DataSnapshot dados: dataSnapshot.child(chosenUser.getId()).getChildren()){
                    Message message = dados.getValue(Message.class);
                    msgList.add(message);
                }

                adapter = new MessagesListAdapter<>(String.valueOf(user.getId()), new MessageHolders(), imageLoader);
                messagesList.setAdapter(adapter);
                adapter.addToEnd(msgList, false);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Utils.showToast(R.string.toast_failLoadingData, ChatActivity.this);
            }
        };

        mRef.addValueEventListener(valueEventListenerMensagem);

    }

//    private void collectMessages(Map<String,Object> msgs) {
//        //iterate through each message, ignoring their UID
//        for (Map.Entry<String, Object> entry : msgs.entrySet()){
//
//            //Get message map
//            Map singleMsg = (Map) entry.getValue();
//            //Get fields and append to list
//            Message msg = new Message();
//            msg.setCreatedAt((Long) singleMsg.get(("timestamp")));
//            msg.setRequestID((Long) singleMsg.get("requestID"));
//            msg.setMessage((String) singleMsg.get("text"));
//            msg.setUpdatedAt((Long) singleMsg.get("updatedAt"));
//
//            msgList.add(msg);
//        }
//
//    }

    private void setupMsgList(){
        messagesList.setHasFixedSize(true);
        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(ChatActivity.this);
        mLayoutManager.setReverseLayout(true);
        messagesList.setLayoutManager(mLayoutManager);

        messagesList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (mIsLoading)
                    return;

                int visibleItemCount = mLayoutManager.getChildCount();
                int totalItemCount = mLayoutManager.getItemCount();
                int pastVisibleItems = mLayoutManager.findFirstVisibleItemPosition();
                if (pastVisibleItems + visibleItemCount >= totalItemCount) {
                    //End of list
                    if (isGetMoreItens) {
                        progressBar.setVisibility(View.VISIBLE);
                        ViewGroup.MarginLayoutParams marginLayoutParams =
                                (ViewGroup.MarginLayoutParams) messagesList.getLayoutParams();
                        marginLayoutParams.setMargins(0, 0, 0, 70);
                        messagesList.setLayoutParams(marginLayoutParams);

                        mIsLoading = true;
                    }
                }
            }
        });
    }

    private void createMessageDatabase(Message message){
        try{
            DatabaseReference mRef = FirebaseUtils.getBaseRef().child("messages");

            mRef.child(user.getId()).child(chosenUser.getId()).push().setValue(message);
        } catch(Exception e){
            Utils.showToast(R.string.toast_errorCreatingMessage, ChatActivity.this);
            e.printStackTrace();
        }
    }

    private void saveChatDatabase(Chat chat, String userId, String chosenUserId){
        try{
            DatabaseReference mRef = FirebaseUtils.getBaseRef().child("chats");

            mRef.child(userId).child(chosenUserId).setValue(chat);
        } catch(Exception e){
            Utils.showToast(R.string.toast_errorCreatingChat, ChatActivity.this);
            e.printStackTrace();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mRef.addValueEventListener(valueEventListenerMensagem);
    }

    @Override
    public void onStop() {
        super.onStop();
        mRef.removeEventListener(valueEventListenerMensagem);
    }
}
