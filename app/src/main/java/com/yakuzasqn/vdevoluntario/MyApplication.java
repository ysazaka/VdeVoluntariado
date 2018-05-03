package com.yakuzasqn.vdevoluntario;

import android.app.Application;

import com.bumptech.glide.request.target.ViewTarget;

// Class created to override the setTag behavior of Glide
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ViewTarget.setTagId(R.id.glide_tag);
    }

}