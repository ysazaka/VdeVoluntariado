package com.yakuzasqn.vdevoluntario.view.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

import com.yakuzasqn.vdevoluntario.R;
import com.yakuzasqn.vdevoluntario.support.Constants;

public class InstituteDataActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_institute_data);

        Toolbar toolbar = findViewById(R.id.id_toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        LinearLayout llCheckInstitute = findViewById(R.id.id_check_institute);
        LinearLayout llCreateVolunteerOffer = findViewById(R.id.id_create_volunteer_offer);
        LinearLayout llCreateInstituteDemand = findViewById(R.id.id_create_institute_demand);
        LinearLayout llCheckChat = findViewById(R.id.id_check_chat);
        LinearLayout llManageParticipants = findViewById(R.id.id_manage_participants);

        llCheckInstitute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openNextActivity(Constants.CHECK_INSTITUTE_ACTIVITY);
            }
        });

        llCreateVolunteerOffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openNextActivity(Constants.CREATE_POST_OFFER_ACTIVITY);
            }
        });

        llCreateInstituteDemand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openNextActivity(Constants.CREATE_POST_DEMAND_ACTIVITY);
            }
        });

        llCheckChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openNextActivity(Constants.CHECK_CHAT_ACTIVITY);
            }
        });

        llManageParticipants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openNextActivity(Constants.MANAGE_PARTICIPANTS_ACTIVITY);
            }
        });
    }

    private void openNextActivity(int activityName){
        Intent intent = null;

        switch (activityName){
            case Constants.CHECK_INSTITUTE_ACTIVITY:
                intent = new Intent(InstituteDataActivity.this, CheckInstituteActivity.class);
                break;
            case Constants.CREATE_POST_OFFER_ACTIVITY:
                intent = new Intent(InstituteDataActivity.this, CreatePostActivity.class);
                intent.putExtra("typeOfPost", "OFFER");
                break;
            case Constants.CREATE_POST_DEMAND_ACTIVITY:
                intent = new Intent(InstituteDataActivity.this, CreatePostActivity.class);
                intent.putExtra("typeOfPost", "DEMAND");
                break;
            case Constants.CHECK_CHAT_ACTIVITY:
                intent = new Intent(InstituteDataActivity.this, CheckChatActivity.class);
                break;
            case Constants.MANAGE_PARTICIPANTS_ACTIVITY:
                intent = new Intent(InstituteDataActivity.this, ManageParticipantsActivity.class);
                break;
        }
        startActivity(intent);
    }
}
