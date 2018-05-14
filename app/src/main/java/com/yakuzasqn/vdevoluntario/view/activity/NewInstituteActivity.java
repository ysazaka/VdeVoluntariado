package com.yakuzasqn.vdevoluntario.view.activity;

import android.app.Activity;
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
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.glide.slider.library.svg.GlideApp;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Order;
import com.orhanobut.hawk.Hawk;
import com.yakuzasqn.vdevoluntario.R;
import com.yakuzasqn.vdevoluntario.model.Group;
import com.yakuzasqn.vdevoluntario.support.Constants;
import com.yakuzasqn.vdevoluntario.support.FirebaseUtils;
import com.yakuzasqn.vdevoluntario.util.Utils;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class NewInstituteActivity extends AppCompatActivity implements Validator.ValidationListener {


    @Order(1)
    @NotEmpty(message = "Campo obrigatório")
    private EditText niEtName;

    private Spinner niSpinCategory;

    private CircleImageView niCivPhoto;
    private EditText niEtAdress, niEtSite, niEtPhone;
    //    private ImageView niPhoto1, niPhoto2, niPhoto3, niPhoto4;

    List<String> listCategory;
    private String name, adress, site, phone, category;

    private Group group;

    private Validator validator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_institute);

        Toolbar toolbar = findViewById(R.id.ni_toolbar);
        toolbar.setTitle("Nova instituição");
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
        toolbar.setNavigationIcon(R.mipmap.ic_arrow_white);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        niCivPhoto = findViewById(R.id.ni_civ_photo);
        niEtName = findViewById(R.id.ni_et_name);
        niEtAdress = findViewById(R.id.ni_et_adress);
        niEtSite = findViewById(R.id.ni_et_site);
        niEtPhone = findViewById(R.id.ni_et_phone);
        niSpinCategory = findViewById(R.id.ni_spin_category);
        Button btnNext = findViewById(R.id.ni_btn_next);
//        niPhoto1 = findViewById(R.id.ni_imageview_1);
//        niPhoto2 = findViewById(R.id.ni_imageview_2);
//        niPhoto3 = findViewById(R.id.ni_imageview_3);
//        niPhoto4 = findViewById(R.id.ni_imageview_4);

        setSpinner();

        validator = new Validator(this);
        validator.setValidationListener(this);

        niCivPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = niEtName.getText().toString();
                adress = niEtAdress.getText().toString();
                site = niEtSite.getText().toString();
                phone = niEtPhone.getText().toString();
                category = listCategory.get(niSpinCategory.getSelectedItemPosition());

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

    private void setSpinner(){
        listCategory = new ArrayList<>();
        listCategory.add("Categoria*");
        listCategory.add("Instituição");
        listCategory.add("ONG");
        listCategory.add("Igreja");
        listCategory.add("Creche");
        listCategory.add("Asilo");
        listCategory.add("Outro");

        ArrayAdapter<String> adapterCategory = new ArrayAdapter<String>(this, R.layout.spinner_item, listCategory){
            @Override
            public boolean isEnabled(int position) {
                if (position == 0){
                    // Disable the item in position 0
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(getResources().getColor(R.color.colorMountainMist));
                }
                else {
                    tv.setTextColor(getResources().getColor(R.color.colorBlack));
                }
                return view;
            }

        };

        adapterCategory.setDropDownViewResource(R.layout.spinner_dropdown_item);
        niSpinCategory.setAdapter(adapterCategory);
    }

    @Override
    public void onValidationSucceeded() {
        Drawable icCamera = getResources().getDrawable(R.drawable.ic_action_camera);
        Bitmap bitPhoto = ((BitmapDrawable) niCivPhoto.getDrawable()).getBitmap();
        Bitmap bitPhotoDefault = ((BitmapDrawable) icCamera).getBitmap();

        if (bitPhoto != bitPhotoDefault){
            if (!category.equals("Category*")){
                group = new Group();

                uploadInstituteProfilePhoto();
            } else {
                Utils.showDialogCustomMessage(R.string.dialog_chooseCategory, NewInstituteActivity.this);
            }
        } else {
            Utils.showDialogCustomMessage(R.string.dialog_chooseProfilePhotoInstitute, NewInstituteActivity.this);
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

    private void uploadInstituteProfilePhoto(){
        String timestamp = Utils.getCurrentTimestamp();

        StorageReference mStoreRef = FirebaseUtils.getFirebaseStorageReference()
                .child("instituteProfilePhoto/" + name + "_" + timestamp + ".jpg");

        niCivPhoto.setDrawingCacheEnabled(true);
        niCivPhoto.buildDrawingCache();

        Bitmap bitmap = niCivPhoto.getDrawingCache();

        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArray);
        byte[] data = byteArray.toByteArray();

        UploadTask uploadTask = mStoreRef.putBytes(data);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Utils.showToast(e.getMessage(), NewInstituteActivity.this);
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                group.setPicture(downloadUrl.toString());
                group.setName(name);
                if (adress != null)
                    group.setAdress(adress);
                if (site != null)
                    group.setSite(site);
                if (phone != null)
                    group.setPhone(phone);
                if (category != null)
                    group.setCategory(category);

                if (group != null){
                    Hawk.put(Constants.GROUP, group);

                    Intent intent = new Intent(NewInstituteActivity.this, CreateInstituteActivity.class);
                    startActivity(intent);
                } else {
                    Utils.showToast("Falha ao criar o grupo!", NewInstituteActivity.this);
                }
            }
        });
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
            GlideApp.with(getApplicationContext()).load(selectedImage).centerCrop().into(niCivPhoto);
        }
    }
}
