package com.yakuzasqn.vdevoluntario.view.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.orhanobut.hawk.Hawk;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;
import com.yakuzasqn.vdevoluntario.R;
import com.yakuzasqn.vdevoluntario.model.User;
import com.yakuzasqn.vdevoluntario.support.Constants;
import com.yakuzasqn.vdevoluntario.support.FirebaseUtils;
import com.yakuzasqn.vdevoluntario.view.fragment.MessageListFragment;
import com.yakuzasqn.vdevoluntario.view.fragment.ConfigFragment;
import com.yakuzasqn.vdevoluntario.view.fragment.InstituteFragment;
import com.yakuzasqn.vdevoluntario.view.fragment.PostFragment;

public class MainActivity extends AppCompatActivity {

    private BottomBar mBottomBar;

    private User user;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser firebaseUser;

    private ProgressDialog dialog;

    private String userUid, userName, userPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        user = Hawk.get(Constants.USER_SESSION);

        mAuth = FirebaseUtils.getFirebaseAuth();

        // Commented because of a bug on Firebase that the Current User's display name comes null
//        setAuthStateListener();

        //BottomBar menu
        mBottomBar = findViewById(R.id.bottomBar);
        mBottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(int tabId) {
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                switch (tabId) {
                    case R.id.b_posts:
                        PostFragment postFragment = new PostFragment();
                        fragmentTransaction.replace(R.id.container, postFragment);
                        fragmentTransaction.commit();
                        break;

                    case R.id.b_message_list:
                        MessageListFragment messageListFragment = new MessageListFragment();
                        fragmentTransaction.replace(R.id.container, messageListFragment);
                        fragmentTransaction.commit();
                        break;

                    case R.id.b_config:
                        ConfigFragment configFragment = new ConfigFragment();
                        fragmentTransaction.replace(R.id.container, configFragment);
                        fragmentTransaction.commit();
                        break;

                    case R.id.b_institute:
                        InstituteFragment instituteFragment = new InstituteFragment();
                        fragmentTransaction.replace(R.id.container, instituteFragment);
                        fragmentTransaction.commit();
                        break;
                }

            }

        });

    }

//    @Override
//    public void onStart() {
//        super.onStart();
//        mAuth.addAuthStateListener(authStateListener);
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//        mAuth.removeAuthStateListener(authStateListener);
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_CODE_MAIN){
            mBottomBar.selectTabAtPosition(0);
        }
    }

    private void setAuthStateListener(){
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                firebaseUser = mAuth.getCurrentUser();
                if (firebaseUser != null){
                    userUid = firebaseUser.getUid();
                    user.setId(userUid);

                    userName = firebaseUser.getDisplayName();
                    user.setName(userName);

                    setValueEventListener();
                }
            }
        };
    }

    private void setValueEventListener(){
        DatabaseReference mRef = FirebaseUtils.getUsersRef();
        mRef.orderByChild("email").equalTo(firebaseUser.getEmail()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot: dataSnapshot.getChildren()) {
                    dialog = ProgressDialog.show(MainActivity.this, "", "Carregando, aguarde...", true);
                    User user1 = userSnapshot.getValue(User.class);
                    userPhoto = user1.getPicture();
                    user.setPicture(userPhoto);

                    Hawk.put(Constants.USER_SESSION, user);
                    dialog.dismiss();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
