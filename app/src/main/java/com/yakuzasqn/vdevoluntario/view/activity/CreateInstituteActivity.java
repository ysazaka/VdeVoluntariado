package com.yakuzasqn.vdevoluntario.view.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.glide.slider.library.svg.GlideApp;
import com.google.firebase.database.DatabaseReference;
import com.orhanobut.hawk.Hawk;
import com.yakuzasqn.vdevoluntario.R;
import com.yakuzasqn.vdevoluntario.model.Group;
import com.yakuzasqn.vdevoluntario.model.User;
import com.yakuzasqn.vdevoluntario.support.Constants;
import com.yakuzasqn.vdevoluntario.support.FirebaseUtils;
import com.yakuzasqn.vdevoluntario.util.Utils;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CreateInstituteActivity extends AppCompatActivity {

    private Group group;
    private List<String> participantsIdList;
    private List<User> participantsList;
    private ProgressDialog dialog;
    private RecyclerView rvParticipants;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_institute);

        Utils.setBackableToolbar(R.id.ci_toolbar, "Criar grupo", CreateInstituteActivity.this);

        group = Hawk.get(Constants.GROUP);

        CircleImageView ciCivPhoto = findViewById(R.id.ci_civ_photo);
        TextView ciName = findViewById(R.id.ci_tv_name);
        TextView ciAdress = findViewById(R.id.ci_tv_adress);
        TextView ciSite = findViewById(R.id.ci_tv_site);
        TextView ciPhone = findViewById(R.id.ci_tv_phone);
        TextView ciCategory = findViewById(R.id.ci_tv_category);
        Button ciBtnCreate = findViewById(R.id.ci_btn_create);
        LinearLayout llAddParticipants = findViewById(R.id.ci_ll_add_participants);
        rvParticipants = findViewById(R.id.rv_participants);

        GlideApp.with(getApplicationContext()).load(group.getPicture()).centerCrop().into(ciCivPhoto);
        ciName.setText(group.getName());
        ciAdress.setText(group.getAdress());
        ciSite.setText(group.getSite());
        ciPhone.setText(group.getPhone());
        ciCategory.setText(group.getArea());

        llAddParticipants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CreateInstituteActivity.this, ChooseParticipantsActivity.class);
                startActivityForResult(intent, Constants.REQUEST_CODE_CREATE_INSTITUTE);
            }
        });

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_CODE_CREATE_INSTITUTE && resultCode == RESULT_OK){
            participantsList = Hawk.get(Constants.CHOSEN_PARTICIPANTS);
            participantsIdList = Hawk.get(Constants.CHOSEN_PARTICIPANTS_ID);

            group.setParticipantsId(participantsIdList);

            if (participantsList != null && participantsIdList != null){
                rvParticipants.setVisibility(View.VISIBLE);
            }
        }

    }

    private void createGroupDatabase(Group group){
        try{
            dialog = ProgressDialog.show(CreateInstituteActivity.this, "", "Cadastrando grupo, aguarde...", true);

            DatabaseReference mRef = FirebaseUtils.getBaseRef().child("groups");

            // Salvar o grupo
            String key = mRef.push().getKey();
            group.setId(key);
            mRef.child(key).setValue(group);

            // Salvar o grupo para o usuário
            Utils.showToast(R.string.toast_created, CreateInstituteActivity.this);
            dialog.dismiss();

            Intent intent = new Intent(CreateInstituteActivity.this, MainActivity.class);
            startActivity(intent);
            finish();


        } catch(Exception e){
            Utils.showToast(R.string.toast_errorCreateGroup, CreateInstituteActivity.this);
            e.printStackTrace();
        }
    }

}
