package com.yakuzasqn.vdevoluntario.view.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        Hawk.init(this).build();
//        User userSession = Hawk.get(LoginActivity.USER_SESSION);
//
        Intent intent;
//
//        if (userSession != null) {
            intent = new Intent(this, LoginActivity.class);
//            intent = new Intent(this, MainActivity.class);
//        } else {
//            intent = new Intent(this, InicioActivity.class);
//        }
        startActivity(intent);
        finish();
    }
}
