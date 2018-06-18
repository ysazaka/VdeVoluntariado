package com.yakuzasqn.vdevoluntario.view.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.orhanobut.hawk.Hawk;
import com.yakuzasqn.vdevoluntario.R;
import com.yakuzasqn.vdevoluntario.model.User;
import com.yakuzasqn.vdevoluntario.support.Constants;
import com.yakuzasqn.vdevoluntario.support.FirebaseUtils;
import com.yakuzasqn.vdevoluntario.util.SupportPermissions;
import com.yakuzasqn.vdevoluntario.util.Utils;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private TextView tvForgotPassword, tvCreateAccount;
    private Button btnSignIn;

    private User user;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.login_et_email);
        etPassword = findViewById(R.id.login_et_password);
        tvForgotPassword = findViewById(R.id.login_tv_forgot_password);
        tvCreateAccount = findViewById(R.id.login_tv_create_account);
        btnSignIn = findViewById(R.id.login_btn_sign_in);

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
        FirebaseAuth mAuth = FirebaseUtils.getFirebaseAuth();
        mAuth.signInWithEmailAndPassword(user.getEmail(), user.getPassword()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    dialog.dismiss();
                    openNextActivity(Constants.MAIN_ACTIVITY);
                    Utils.showToast(R.string.toast_loginValidate, LoginActivity.this);
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
                Hawk.put(Constants.USER_SESSION ,user);
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

    private void verifyPermissions(){
        /* Permissões */
        SupportPermissions permissions = new SupportPermissions();
        permissions.requestForPermission(LoginActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE);
    }
}
