package com.yakuzasqn.vdevoluntario.view.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.orhanobut.hawk.Hawk;
import com.yakuzasqn.vdevoluntario.R;
import com.yakuzasqn.vdevoluntario.model.Chat;
import com.yakuzasqn.vdevoluntario.model.User;
import com.yakuzasqn.vdevoluntario.support.Constants;
import com.yakuzasqn.vdevoluntario.support.FirebaseUtils;
import com.yakuzasqn.vdevoluntario.util.SupportPermissions;
import com.yakuzasqn.vdevoluntario.util.Utils;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;

    private User user;
    private ProgressDialog dialog;
    private List<User> userList;

    private DatabaseReference mRef;
    private ValueEventListener valueEventListenerGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.login_et_email);
        etPassword = findViewById(R.id.login_et_password);
        TextView tvForgotPassword = findViewById(R.id.login_tv_forgot_password);
        TextView tvCreateAccount = findViewById(R.id.login_tv_create_account);
        Button btnSignIn = findViewById(R.id.login_btn_sign_in);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!etEmail.getText().toString().equals("") && !etPassword.getText().toString().equals("")){
                    dialog = ProgressDialog.show(LoginActivity.this, "", "Fazendo login, aguarde...", true);

                    user = new User();
                    user.setEmail(etEmail.getText().toString());
                    user.setPassword(etPassword.getText().toString());

                    validateLogin(user);
                } else {
                    Utils.showToast("Preencha todos os oampos", LoginActivity.this);
                }
            }
        });

        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openNextActivity(Constants.FORGOT_PASSWORD_ACTIVITY);
            }
        });

        tvCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openNextActivity(Constants.CREATE_ACCOUNT_ACTIVITY);
            }
        });

        verifyPermissions();
    }

    private void validateLogin(User user){
        final FirebaseAuth mAuth = FirebaseUtils.getFirebaseAuth();
        mAuth.signInWithEmailAndPassword(user.getEmail(), user.getPassword()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
//                    String t = mAuth.getCurrentUser().getDisplayName();
                    getUserData();
                } else {
                    dialog.dismiss();
                    Utils.showToast(R.string.toast_loginInvalidate, LoginActivity.this);
                }
            }
        });
    }

    private void openNextActivity(int activityName){
        Intent intent = null;

        switch (activityName){
            case Constants.MAIN_ACTIVITY:
                intent = new Intent(LoginActivity.this, MainActivity.class);
                finish();
                break;
            case Constants.FORGOT_PASSWORD_ACTIVITY:
                intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                break;
            case Constants.CREATE_ACCOUNT_ACTIVITY:
                intent = new Intent(LoginActivity.this, CreateAccountActivity.class);
                break;
        }
        startActivity(intent);
    }

    private void getUserData(){
        mRef = FirebaseUtils.getBaseRef().child("users");
        userList = new ArrayList<>();

        valueEventListenerGroup = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userList.clear();
                for (DataSnapshot dados: dataSnapshot.getChildren()){
                    User user = dados.getValue(User.class);
                    userList.add(user);
                }

                for (int i = 0; i < userList.size(); i++){
                    if (userList.get(i).getEmail().equals(user.getEmail())){
                        user.setId(userList.get(i).getId());
                        user.setName(userList.get(i).getName());
                        user.setPicture(userList.get(i).getPicture());
                        user.setGroupsId(userList.get(i).getGroupsId());
                    }
                }
                Hawk.put(Constants.USER_SESSION ,user);
                dialog.dismiss();
                openNextActivity(Constants.MAIN_ACTIVITY);
                Utils.showToast(R.string.toast_loginValidate, LoginActivity.this);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(LoginActivity.this, "Falha ao recuperar dados", Toast.LENGTH_SHORT).show();
            }
        };

        mRef.addValueEventListener(valueEventListenerGroup);
    }


    private void verifyPermissions(){
        /* Permissões */
        SupportPermissions permissions = new SupportPermissions();
        permissions.requestForPermission(LoginActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE);
    }
}
