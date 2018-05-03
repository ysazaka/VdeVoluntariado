package com.yakuzasqn.vdevoluntario.view.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.yakuzasqn.vdevoluntario.R;
import com.yakuzasqn.vdevoluntario.model.User;
import com.yakuzasqn.vdevoluntario.support.FirebaseConfig;
import com.yakuzasqn.vdevoluntario.util.Utils;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private TextView tvForgotPassword, tvCreateAccount;
    private Button btnSignIn;

    private final int MAIN_ACTIVITY = 1;
    private final int FORGOT_PASSWORD_ACTIVITY = 2;
    private final int CREATE_ACCOUNT_ACTIVITY = 3;

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
                User user = new User();
                user.setEmail(etEmail.getText().toString());
                user.setPassword(etPassword.getText().toString());

                validateLogin(user);
            }
        });

        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openNextActivity(FORGOT_PASSWORD_ACTIVITY);
            }
        });

        tvCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openNextActivity(CREATE_ACCOUNT_ACTIVITY);
            }
        });
    }

    private void validateLogin(User user){
        FirebaseAuth mAuth = FirebaseConfig.getFirebaseAuth();
        mAuth.signInWithEmailAndPassword(user.getEmail(), user.getPassword()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    openNextActivity(MAIN_ACTIVITY);
                    Utils.showToast(R.string.toast_loginValidate, LoginActivity.this);
                } else {
                    Utils.showToast(R.string.toast_loginInvalidate, LoginActivity.this);
                }
            }
        });
    }

    private void openNextActivity(int activityName){
        Intent intent = null;

        switch (activityName){
            case MAIN_ACTIVITY:
                intent = new Intent(LoginActivity.this, MainActivity.class);
                break;
            case FORGOT_PASSWORD_ACTIVITY:
                intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                break;
            case CREATE_ACCOUNT_ACTIVITY:
                intent = new Intent(LoginActivity.this, CreateAccountActivity.class);
                break;
        }
        startActivity(intent);
    }
}
