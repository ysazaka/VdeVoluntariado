package com.yakuzasqn.vdevoluntario.view.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.glide.slider.library.svg.GlideApp;
import com.orhanobut.hawk.Hawk;
import com.yakuzasqn.vdevoluntario.R;
import com.yakuzasqn.vdevoluntario.model.Group;
import com.yakuzasqn.vdevoluntario.support.Constants;
import com.yakuzasqn.vdevoluntario.util.Utils;

import de.hdodenhof.circleimageview.CircleImageView;

public class InstituteDataActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_institute_data);

        Group group = Hawk.get(Constants.CHOSEN_GROUP);

        Utils.setBackableToolbar(R.id.id_toolbar, "", InstituteDataActivity.this);

        LinearLayout llCheckInstitute = findViewById(R.id.id_check_institute);
        LinearLayout llManageDemand = findViewById(R.id.id_ll_manage_group_demand);
        LinearLayout llManageOffer = findViewById(R.id.id_ll_manage_group_offer);
        LinearLayout llCheckChat = findViewById(R.id.id_check_chat);
        LinearLayout llManageParticipants = findViewById(R.id.id_manage_participants);
        CircleImageView civPhoto = findViewById(R.id.id_photo);
        TextView tvName = findViewById(R.id.id_name);

        GlideApp.with(getApplicationContext()).load(group.getPicture()).centerCrop().into(civPhoto);
        tvName.setText(group.getName());

        llCheckInstitute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openNextActivity(Constants.CHECK_INSTITUTE_ACTIVITY);
            }
        });

        llCheckChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openNextActivity(Constants.CHECK_CHAT_ACTIVITY);
            }
        });

        llManageDemand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openNextActivity(Constants.MANAGE_GROUP_DEMAND_ACTIVITY);
            }
        });

        llManageOffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openNextActivity(Constants.MANAGE_GROUP_OFFER_ACTIVITY);
            }
        });

        llManageParticipants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openNextActivity(Constants.MANAGE_PARTICIPANTS_ACTIVITY);
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
            case Constants.CHECK_INSTITUTE_ACTIVITY:
                intent = new Intent(InstituteDataActivity.this, CheckInstituteActivity.class);
                break;
//            case Constants.CREATE_POST_OFFER_ACTIVITY:
//                intent = new Intent(InstituteDataActivity.this, CreatePostActivity.class);
//                intent.putExtra("typeOfPost", Constants.OFFER);
//                break;
//            case Constants.CREATE_POST_DEMAND_ACTIVITY:
//                intent = new Intent(InstituteDataActivity.this, CreatePostActivity.class);
//                intent.putExtra("typeOfPost", Constants.DEMAND);
//                break;
            case Constants.CHECK_CHAT_ACTIVITY:
                intent = new Intent(InstituteDataActivity.this, CheckChatActivity.class);
                break;
            case Constants.MANAGE_PARTICIPANTS_ACTIVITY:
                intent = new Intent(InstituteDataActivity.this, ManageParticipantsActivity.class);
                break;
            case Constants.MANAGE_GROUP_DEMAND_ACTIVITY:
                intent = new Intent(InstituteDataActivity.this, ManageGroupDemandActivity.class);
                break;
            case Constants.MANAGE_GROUP_OFFER_ACTIVITY:
                intent = new Intent(InstituteDataActivity.this, ManageGroupOfferActivity.class);
                break;
        }
        startActivity(intent);
    }
}
