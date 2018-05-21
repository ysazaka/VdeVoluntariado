package com.yakuzasqn.vdevoluntario.view.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.yakuzasqn.vdevoluntario.R;
import com.yakuzasqn.vdevoluntario.util.Utils;

public class CheckChatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_chat);

        Utils.setBackableToolbar(R.id.checkchat_toolbar, "Conversas", CheckChatActivity.this);


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
}
