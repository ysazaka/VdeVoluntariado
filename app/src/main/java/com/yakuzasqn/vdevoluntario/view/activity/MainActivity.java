package com.yakuzasqn.vdevoluntario.view.activity;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;
import com.yakuzasqn.vdevoluntario.R;
import com.yakuzasqn.vdevoluntario.model.User;
import com.yakuzasqn.vdevoluntario.view.fragment.MessageListFragment;
import com.yakuzasqn.vdevoluntario.view.fragment.ConfigFragment;
import com.yakuzasqn.vdevoluntario.view.fragment.InstituteFragment;
import com.yakuzasqn.vdevoluntario.view.fragment.PostFragment;

public class MainActivity extends AppCompatActivity {

    private BottomBar mBottomBar;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
}
