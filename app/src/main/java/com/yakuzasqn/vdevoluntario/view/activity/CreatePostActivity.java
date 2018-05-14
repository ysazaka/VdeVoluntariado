package com.yakuzasqn.vdevoluntario.view.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.glide.slider.library.svg.GlideApp;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Order;
import com.orhanobut.hawk.Hawk;
import com.yakuzasqn.vdevoluntario.R;
import com.yakuzasqn.vdevoluntario.model.Post;
import com.yakuzasqn.vdevoluntario.model.User;
import com.yakuzasqn.vdevoluntario.support.Constants;
import com.yakuzasqn.vdevoluntario.support.FirebaseUtils;
import com.yakuzasqn.vdevoluntario.util.Utils;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class CreatePostActivity extends AppCompatActivity  implements Validator.ValidationListener {

    @Order(1)
    @NotEmpty(message = "Campo obrigatório")
    private EditText etPostTitle;

    @Order(2)
    @NotEmpty(message = "Campo obrigatório")
    private EditText etPostDescription;

    private ImageView postPhoto;

    private String title, description;

    private Post post;
    private User user;

    private Validator validator;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        Toolbar toolbar = findViewById(R.id.cp_toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_arrow_white);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        postPhoto = findViewById(R.id.cp_post_photo);
        etPostTitle = findViewById(R.id.cp_post_title);
        etPostDescription = findViewById(R.id.cp_post_description);
        Button btnPublish = findViewById(R.id.cp_publish);

        user = Hawk.get(Constants.USER_SESSION);

        validator = new Validator(this);
        validator.setValidationListener(this);

        postPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });

        btnPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title = etPostTitle.getText().toString();
                description = etPostDescription.getText().toString();
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
        // Verificação para ver se o usuário escolheu uma foto de perfil
        Drawable icCamera = getResources().getDrawable(R.drawable.ic_action_camera);
        Bitmap bitPhoto = ((BitmapDrawable) postPhoto.getDrawable()).getBitmap();
        Bitmap bitPhotoDefault = ((BitmapDrawable) icCamera).getBitmap();

        if (bitPhoto != bitPhotoDefault){
            post = new Post();

            uploadPostPhoto();
        } else {
            Utils.showDialogCustomMessage(R.string.dialog_chooseProfilePhoto, CreatePostActivity.this);
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

    private void uploadPostPhoto(){
        dialog = ProgressDialog.show(CreatePostActivity.this, "", "Fazendo upload da foto, aguarde...", true);
        String timestamp = Utils.getCurrentTimestamp();

        StorageReference mStoreRef = FirebaseUtils.getFirebaseStorageReference()
                .child("userProfilePhoto/post_" + timestamp + ".jpg");

        postPhoto.setDrawingCacheEnabled(true);
        postPhoto.buildDrawingCache();

        Bitmap bitmap = postPhoto.getDrawingCache();

        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArray);
        byte[] data = byteArray.toByteArray();

        UploadTask uploadTask = mStoreRef.putBytes(data);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Utils.showToast(e.getMessage(), CreatePostActivity.this);
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                dialog.dismiss();
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                post.setUrlImage(downloadUrl.toString());
                post.setTitle(title);
                post.setDescription(description);
                post.setUser(user);

                createPostDatabase(post);
            }
        });
    }

    private void createPostDatabase(Post post){
        try{
            DatabaseReference mRef = FirebaseUtils.getBaseRef().child("posts");

            String key = mRef.push().getKey();
            post.setId(key);
            mRef.child(key).setValue(post);

            dialog = ProgressDialog.show(CreatePostActivity.this, "", "Cadastrando postagem, aguarde...", true);
            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    Utils.showToast(R.string.toast_posted, CreatePostActivity.this);
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
        } catch(Exception e){
            Utils.showToast(getString(R.string.toast_errorCreateUser), CreatePostActivity.this);
            e.printStackTrace();
        }
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
            GlideApp.with(getApplicationContext()).load(selectedImage).centerCrop().into(postPhoto);
        }
    }
}
