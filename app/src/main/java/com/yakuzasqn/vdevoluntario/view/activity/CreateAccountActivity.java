package com.yakuzasqn.vdevoluntario.view.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.DatabaseReference;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Order;
import com.orhanobut.hawk.Hawk;
import com.yakuzasqn.vdevoluntario.R;
import com.yakuzasqn.vdevoluntario.model.User;
import com.yakuzasqn.vdevoluntario.support.FirebaseConfig;
import com.yakuzasqn.vdevoluntario.util.Utils;

import java.util.List;

import static com.yakuzasqn.vdevoluntario.support.Constants.USER_SESSION;

public class CreateAccountActivity extends AppCompatActivity implements Validator.ValidationListener {

    @Order(1)
    @NotEmpty(message = "Campo obrigatório")
    private EditText etName;

    @Order(2)
    @NotEmpty(message = "Campo obrigatório")
    private EditText etEmail;

    @Order(3)
    @NotEmpty(message = "Campo obrigatório")
    private EditText etPassword;

    @Order(4)
    @NotEmpty(message = "Campo obrigatório")
    private EditText etPasswordConfirm;

    private String name, email, password, passwordConfirm;

    private Validator validator;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        Toolbar toolbar = findViewById(R.id.ca_toolbar);
        toolbar.setTitle("Cadastro de usuário");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        etName = findViewById(R.id.ca_name);
        etEmail = findViewById(R.id.ca_email);
        etPassword = findViewById(R.id.ca_password);
        etPasswordConfirm = findViewById(R.id.ca_password_confirm);
        Button btnSignUp = findViewById(R.id.ca_sign_up);

        validator = new Validator(this);
        validator.setValidationListener(this);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = etName.getText().toString();
                email = etEmail.getText().toString();
                password = etPassword.getText().toString();
                passwordConfirm = etPasswordConfirm.getText().toString();
                validator.validate();
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
    public void onValidationSucceeded() {
        if (password.equals(passwordConfirm)){
            user = new User();
            user.setName(name);
            user.setEmail(email);
            user.setPassword(password);

            Hawk.put(USER_SESSION, user);

            createUserAuth();
        } else
            Utils.showDialogCustomMessage(R.string.dialog_diffPassword, CreateAccountActivity.this);
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

    private void createUserAuth(){
        final FirebaseAuth mAuth = FirebaseConfig.getFirebaseAuth();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(CreateAccountActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            createUserDatabase(user);

                            // Firebase loga usuário logo após cadastro, então é necessário desconectar
                            mAuth.signOut();
                        } else {
                            String createUserException = "";

                            try {
                                throw task.getException();
                            } catch(FirebaseAuthWeakPasswordException e){
                                createUserException = "Digite uma senha mais forte, contendo no mínimo 8 caracteres e que contenha letras e números!";
                            } catch(FirebaseAuthInvalidCredentialsException e){
                                createUserException = "O e-mail digitado é inválido, digite um novo e-mail";
                            } catch(FirebaseAuthUserCollisionException e){
                                createUserException = "Esse e-mail já foi cadastrado!";
                            } catch (Exception e) {
                                createUserException = "Erro ao efetuar o cadastro!";
                                e.printStackTrace();
                            }

                            Utils.showToast(createUserException, CreateAccountActivity.this);
                        }
                    }
                });
    }

    private void createUserDatabase(User user){
        try{
            DatabaseReference mRef = FirebaseConfig.getDatabaseReference().child("users");

            // Firebase gera uma chave automática e ordena por inserção no banco de dados
            String key = mRef.push().getKey();
            user.setKey(key);
            mRef.child(key).setValue(user);

            Intent intent = new Intent(CreateAccountActivity.this, ConfirmationActivity.class);
            startActivity(intent);
        } catch(Exception e){
            Utils.showToast(getString(R.string.toast_errorCreateUser), CreateAccountActivity.this);
            e.printStackTrace();
        }
    }

}
