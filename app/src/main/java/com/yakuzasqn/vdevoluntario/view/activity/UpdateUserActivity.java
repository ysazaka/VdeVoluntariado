package com.yakuzasqn.vdevoluntario.view.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.orhanobut.hawk.Hawk;
import com.yakuzasqn.vdevoluntario.R;
import com.yakuzasqn.vdevoluntario.model.User;
import com.yakuzasqn.vdevoluntario.support.Constants;
import com.yakuzasqn.vdevoluntario.support.FirebaseUtils;
import com.yakuzasqn.vdevoluntario.util.Utils;

public class UpdateUserActivity extends AppCompatActivity{

    private EditText etNewName, etActualPassword, etNewPassword, etConfirmNewPassword;

    private String newName, actualPassword, newPassword, confirmNewPassword, auxName, auxPassword;

    private ProgressDialog dialog;
    private User user;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user);

        Toolbar toolbar = findViewById(R.id.up_toolbar);
        toolbar.setTitle("Atualizar dados");
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
        toolbar.setNavigationIcon(R.mipmap.ic_arrow_white);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        etNewName = findViewById(R.id.up_et_new_name);
        etActualPassword = findViewById(R.id.up_et_actual_password);
        etNewPassword = findViewById(R.id.up_et_new_password);
        etConfirmNewPassword = findViewById(R.id.up_et_confirm_new_password);
        Button btnUpdate = findViewById(R.id.up_btn_update);

        user = Hawk.get(Constants.USER_SESSION);
        mAuth = FirebaseUtils.getFirebaseAuth();

        auxName = user.getName();
        auxPassword = user.getPassword();

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newName = etNewName.getText().toString();
                actualPassword = etActualPassword.getText().toString();
                newPassword = etNewPassword.getText().toString();
                confirmNewPassword = etConfirmNewPassword.getText().toString();

                // Update name
                if (!newName.equals("") && !newName.equals(user.getName())){
                    updateName();
                } else {
                    Utils.showToast(R.string.toast_sameName, UpdateUserActivity.this);
                }

                // Update password
                if (!actualPassword.equals("")){
                    if (actualPassword.equals(user.getPassword())){
                        if (newPassword.equals(confirmNewPassword)){
                            updatePassword();
                        } else {
                            Utils.showToast(R.string.toast_diferentConfirmationPassword, UpdateUserActivity.this);
                        }
                    } else {
                        Utils.showToast(R.string.toast_diferentActualPassword, UpdateUserActivity.this);
                    }
                }

                // After update, finish the activity
                if (auxName != user.getName() || auxPassword != user.getPassword()){
                    dialog = ProgressDialog.show(UpdateUserActivity.this, "", "Atualizando dados, aguarde...", true);
                    dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            Utils.showToast(R.string.toast_updated, UpdateUserActivity.this);
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
                            2500
                    );
                }
            }
        });

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (mAuth.getCurrentUser() != null){
                    firebaseUser = mAuth.getCurrentUser();
                }
            }
        };
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

    private void updateName(){
        firebaseUser = mAuth.getCurrentUser();
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(newName)
                .build();

        if (firebaseUser != null){
            firebaseUser.updateProfile(profileUpdates);

            user.setName(newName);
            Hawk.put(Constants.USER_SESSION, user);
        } else {
            Utils.showToast("Firebase User = null", UpdateUserActivity.this);
        }
    }

    private void updatePassword(){
        firebaseUser = mAuth.getCurrentUser();

        firebaseUser.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Utils.showToast(R.string.toast_passwordUpdated, UpdateUserActivity.this);
                } else {
                    Utils.showToast(R.string.toast_passwordNotUpdated, UpdateUserActivity.this);
                }
            }
        });

        user.setPassword(newPassword);
        Hawk.put(Constants.USER_SESSION, user);
    }
}
