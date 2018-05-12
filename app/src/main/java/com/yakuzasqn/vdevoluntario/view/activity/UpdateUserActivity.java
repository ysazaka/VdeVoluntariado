package com.yakuzasqn.vdevoluntario.view.activity;

import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Order;
import com.orhanobut.hawk.Hawk;
import com.yakuzasqn.vdevoluntario.R;
import com.yakuzasqn.vdevoluntario.model.User;
import com.yakuzasqn.vdevoluntario.support.Constants;
import com.yakuzasqn.vdevoluntario.support.FirebaseConfig;
import com.yakuzasqn.vdevoluntario.util.Utils;

import java.util.List;

public class UpdateUserActivity extends AppCompatActivity  implements Validator.ValidationListener {

    @Order(1)
    @NotEmpty(message = "Campo obrigatório")
    private EditText etNewName;

    @Order(2)
    @NotEmpty(message = "Campo obrigatório")
    private EditText etActualPassword;

    @Order(3)
    @NotEmpty(message = "Campo obrigatório")
    private EditText etNewPassword;

    @Order(4)
    @NotEmpty(message = "Campo obrigatório")
    private EditText etConfirmNewPassword;

    private String newName, actualPassword, newPassword, confirmNewPassword;

    private Validator validator;

    private User user;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user);

        Toolbar toolbar = findViewById(R.id.up_toolbar);
        toolbar.setTitle("Atualizar dados");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        etNewName = findViewById(R.id.up_et_new_name);
        etActualPassword = findViewById(R.id.up_et_actual_password);
        etNewPassword = findViewById(R.id.up_et_new_password);
        etConfirmNewPassword = findViewById(R.id.up_et_confirm_new_password);
        Button btnUpdate = findViewById(R.id.up_btn_update);

        user = Hawk.get(Constants.USER_SESSION);
        mAuth = FirebaseConfig.getFirebaseAuth();

        validator = new Validator(this);
        validator.setValidationListener(this);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newName = etNewName.getText().toString();
                actualPassword = etActualPassword.getText().toString();
                newPassword = etNewPassword.getText().toString();
                confirmNewPassword = etConfirmNewPassword.getText().toString();

                validator.validate();
            }
        });
    }

    @Override
    public void onValidationSucceeded() {
        // Update name
        if (!newName.equals(user.getName())){
            updateName();
        }

        // Update password
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

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            view.requestFocus();
            String message = error.getCollatedErrorMessage(this);

            // Display error messages
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Snackbar.make(error.getView(), message, Snackbar.LENGTH_SHORT).show();
            }
        }
    }

    private void updateName(){
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(newName)
                .build();

        firebaseUser.updateProfile(profileUpdates);

        user.setName(newName);
        Hawk.put(Constants.USER_SESSION, user);
    }

    private void updatePassword(){
        FirebaseUser firebaseUser = mAuth.getCurrentUser();

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
