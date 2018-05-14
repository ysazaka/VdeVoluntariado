package com.yakuzasqn.vdevoluntario.view.activity;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.yakuzasqn.vdevoluntario.R;
import com.yakuzasqn.vdevoluntario.support.FirebaseUtils;
import com.yakuzasqn.vdevoluntario.util.Utils;

public class ForgotPasswordActivity extends AppCompatActivity {

    private LinearLayout llForgotPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        final EditText etEmail = findViewById(R.id.fp_et_email);
        Button btnSend = findViewById(R.id.fp_btn_send);
        llForgotPassword = findViewById(R.id.fp_ll);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!etEmail.getText().equals("")){
                    FirebaseAuth mAuth = FirebaseUtils.getFirebaseAuth();

                    String emailToRecover = etEmail.getText().toString();

                    mAuth.sendPasswordResetEmail(emailToRecover).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Utils.showSnackbarMessage(R.string.dialog_verifyEmail, ForgotPasswordActivity.this, llForgotPassword);
                            } else {
                                Utils.showSnackbarMessage(R.string.snackbar_sendEmailFail, ForgotPasswordActivity.this, llForgotPassword);
                            }
                        }
                    });

                } else {
                    Utils.showToast(getString(R.string.toast_fillEmail), ForgotPasswordActivity.this);
                }
            }
        });
    }
}
