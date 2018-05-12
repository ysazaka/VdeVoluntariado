package com.yakuzasqn.vdevoluntario.view.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

import com.yakuzasqn.vdevoluntario.R;
import com.yakuzasqn.vdevoluntario.support.Constants;

public class UserDataActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_data);

        Toolbar toolbar = findViewById(R.id.ca_toolbar);
        toolbar.setTitle("Ajustes");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        LinearLayout llUpdatePassword = findViewById(R.id.ud_update_user);

        llUpdatePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openNextActivity(Constants.UPDATE_USER_ACTIVITY);
            }
        });
    }

    private void openNextActivity(int activityName){
        Intent intent = null;

        switch (activityName){
            case Constants.UPDATE_USER_ACTIVITY:
                intent = new Intent(UserDataActivity.this, UpdateUserActivity.class);
                break;
        }
        startActivity(intent);
    }
}
