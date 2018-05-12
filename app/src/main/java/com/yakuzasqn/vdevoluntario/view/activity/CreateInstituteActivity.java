package com.yakuzasqn.vdevoluntario.view.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.glide.slider.library.svg.GlideApp;
import com.google.firebase.database.DatabaseReference;
import com.orhanobut.hawk.Hawk;
import com.yakuzasqn.vdevoluntario.R;
import com.yakuzasqn.vdevoluntario.model.Group;
import com.yakuzasqn.vdevoluntario.support.Constants;
import com.yakuzasqn.vdevoluntario.support.FirebaseConfig;
import com.yakuzasqn.vdevoluntario.util.Utils;

import de.hdodenhof.circleimageview.CircleImageView;

public class CreateInstituteActivity extends AppCompatActivity {

    private Group group;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_institute);

        Toolbar toolbar = findViewById(R.id.ci_toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setTitle("Criar instituição");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        group = Hawk.get(Constants.GROUP);

        CircleImageView ciCivPhoto = findViewById(R.id.ci_civ_photo);
        TextView ciName = findViewById(R.id.ci_tv_name);
        TextView ciAdress = findViewById(R.id.ci_tv_adress);
        TextView ciSite = findViewById(R.id.ci_tv_site);
        TextView ciPhone = findViewById(R.id.ci_tv_phone);
        TextView ciCategory = findViewById(R.id.ci_tv_category);
        Button ciBtnCreate = findViewById(R.id.ci_btn_create);

        GlideApp.with(getApplicationContext()).load(group.getPicture()).centerCrop().into(ciCivPhoto);
        ciName.setText(group.getName());
        ciAdress.setText(group.getAdress());
        ciSite.setText(group.getSite());
        ciPhone.setText(group.getPhone());
        ciCategory.setText(group.getCategory());

        ciBtnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createGroupDatabase(group);
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

    // TODO: Verificar como fazer a estrutura das instituições no Firebase
    private void createGroupDatabase(Group group){
        try{
            DatabaseReference mRef = FirebaseConfig.getDatabaseReference().child("groups");

            // Firebase gera uma chave automática e ordena por inserção no banco de dados
            String key = mRef.push().getKey();
            group.setId(key);
            mRef.child(key).setValue(group);

            dialog = ProgressDialog.show(CreateInstituteActivity.this, "", "Criando instituição, aguarde...", true);
            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    Utils.showToast(R.string.toast_posted, CreateInstituteActivity.this);
                    Intent intent = new Intent(CreateInstituteActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            });

            new java.util.Timer().schedule(
                    new java.util.TimerTask() {
                        @Override
                        public void run() {
                            if( dialog != null ) dialog.dismiss();
                        }
                    },
                    5000
            );

        } catch(Exception e){
            Utils.showToast(R.string.toast_errorCreateUser, CreateInstituteActivity.this);
            e.printStackTrace();
        }
    }

}
