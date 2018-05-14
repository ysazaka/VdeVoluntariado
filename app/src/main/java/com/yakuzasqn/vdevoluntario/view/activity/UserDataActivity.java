package com.yakuzasqn.vdevoluntario.view.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.glide.slider.library.svg.GlideApp;
import com.orhanobut.hawk.Hawk;
import com.yakuzasqn.vdevoluntario.R;
import com.yakuzasqn.vdevoluntario.model.User;
import com.yakuzasqn.vdevoluntario.support.Constants;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserDataActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_data);

        User user = Hawk.get(Constants.USER_SESSION);

        Toolbar toolbar = findViewById(R.id.ud_toolbar);
        toolbar.setTitle(user.getName());
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
        toolbar.setNavigationIcon(R.mipmap.ic_arrow_white);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CircleImageView civUserPhoto = findViewById(R.id.ud_photo);

        GlideApp.with(getApplicationContext()).load(user.getPicture()).centerCrop().into(civUserPhoto);

        LinearLayout llUpdatePassword = findViewById(R.id.ud_update_user);

        llUpdatePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openNextActivity(Constants.UPDATE_USER_ACTIVITY);
            }
        });
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

    private void openNextActivity(int activityName){
        Intent intent = null;

        switch (activityName){
            case Constants.UPDATE_USER_ACTIVITY:
                intent = new Intent(UserDataActivity.this, UpdateUserActivity.class);
                break;
        }
        startActivityForResult(intent, Constants.REQUEST_CODE_USER_DATA);
    }

}
