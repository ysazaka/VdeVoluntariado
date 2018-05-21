package com.yakuzasqn.vdevoluntario.view.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.glide.slider.library.svg.GlideApp;
import com.orhanobut.hawk.Hawk;
import com.yakuzasqn.vdevoluntario.R;
import com.yakuzasqn.vdevoluntario.model.Group;
import com.yakuzasqn.vdevoluntario.support.Constants;
import com.yakuzasqn.vdevoluntario.util.Utils;

import de.hdodenhof.circleimageview.CircleImageView;

public class CheckInstituteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_institute);

        Group group = Hawk.get(Constants.CHOSEN_GROUP);

        Utils.setBackableToolbar(R.id.checkin_toolbar, "Dados da instituição", CheckInstituteActivity.this);

        CircleImageView civPhoto = findViewById(R.id.checkin_civ_photo);
        EditText etName = findViewById(R.id.checkin_et_name);
        EditText etAdress = findViewById(R.id.checkin_et_adress);
        EditText etSite = findViewById(R.id.checkin_et_site);
        EditText etPhone = findViewById(R.id.checkin_et_phone);
        TextView tvArea = findViewById(R.id.checkin_tv_area);
        Button btnUpdate = findViewById(R.id.checkin_btn_update);

        GlideApp.with(getApplicationContext()).load(group.getPicture()).centerCrop().into(civPhoto);
        etName.setText(group.getName());
        etAdress.setText(group.getAdress());
        etSite.setText(group.getSite());
        etPhone.setText(group.getPhone());
        tvArea.setText(group.getArea());

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //
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
}
