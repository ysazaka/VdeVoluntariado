package com.yakuzasqn.vdevoluntario.view.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.glide.slider.library.svg.GlideApp;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.orhanobut.hawk.Hawk;
import com.yakuzasqn.vdevoluntario.R;
import com.yakuzasqn.vdevoluntario.model.Post;
import com.yakuzasqn.vdevoluntario.model.User;
import com.yakuzasqn.vdevoluntario.support.Constants;
import com.yakuzasqn.vdevoluntario.support.FirebaseUtils;
import com.yakuzasqn.vdevoluntario.util.Utils;

import java.io.ByteArrayOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.yakuzasqn.vdevoluntario.support.Constants.USER_SESSION;

public class UpdateUserActivity extends AppCompatActivity{

    private EditText etNewName, etActualPassword, etNewPassword, etConfirmNewPassword;
    private CircleImageView civUserPhoto;

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

        Utils.setBackableToolbar(R.id.up_toolbar, "Atualizar dados", UpdateUserActivity.this);

        etNewName = findViewById(R.id.up_et_new_name);
        etActualPassword = findViewById(R.id.up_et_actual_password);
        etNewPassword = findViewById(R.id.up_et_new_password);
        etConfirmNewPassword = findViewById(R.id.up_et_confirm_new_password);
        Button btnUpdate = findViewById(R.id.up_btn_update);

        user = Hawk.get(Constants.USER_SESSION);
        mAuth = FirebaseUtils.getFirebaseAuth();

        civUserPhoto = findViewById(R.id.up_photo);
        GlideApp.with(getApplicationContext()).load(user.getPicture()).centerCrop().into(civUserPhoto);

        civUserPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                civUserPhoto.setSelected(true);
                openGallery();
            }
        });

        auxName = user.getName();
        auxPassword = user.getPassword();

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newName = etNewName.getText().toString();
                actualPassword = etActualPassword.getText().toString();
                newPassword = etNewPassword.getText().toString();
                confirmNewPassword = etConfirmNewPassword.getText().toString();

                // Update profile photo
                if (civUserPhoto.isSelected())
                    uploadProfilePhoto();

                // Update name
                if (!newName.equals("")){
                    if (!newName.equals(user.getName())){
                        updateName();
                    } else {
                        Utils.showToast(R.string.toast_sameName, UpdateUserActivity.this);
                    }
                } else{
                    Utils.showToast(R.string.toast_emptyName, UpdateUserActivity.this);
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
                if (civUserPhoto.isSelected() || !auxName.equals(user.getName()) || !auxPassword.equals(user.getPassword())){
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

    private void openGallery(){
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        i.setType("image/*");
        startActivityForResult(i, Constants.REQUEST_CODE_GALLERY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_CODE_GALLERY && resultCode == Activity.RESULT_OK){
            Uri selectedImage = data.getData();
            GlideApp.with(getApplicationContext()).load(selectedImage).centerCrop().into(civUserPhoto);
        }
    }

    private void uploadProfilePhoto(){
        StorageReference mStoreRef = FirebaseUtils.getFirebaseStorageReference()
                .child("userProfilePhoto/" + user.getEmail() + ".jpg");

        civUserPhoto.setDrawingCacheEnabled(true);
        civUserPhoto.buildDrawingCache();

        Bitmap bitmap = civUserPhoto.getDrawingCache();

        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArray);
        byte[] data = byteArray.toByteArray();

        UploadTask uploadTask = mStoreRef.putBytes(data);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Utils.showToast(e.getMessage(), UpdateUserActivity.this);
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                user.setPicture(downloadUrl.toString());

                firebaseUser = mAuth.getCurrentUser();
//                firebaseUser.updateProfile({
//                        displayName: displayName,
//                        photoURL: photoURL
//                });

                Hawk.put(USER_SESSION, user);
            }
        });

    }
}
