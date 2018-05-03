package com.yakuzasqn.vdevoluntario.support;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * Created by yoshi on 21/04/2018.
 */

public class FirebaseConfig {
    private static DatabaseReference mRef;
    private static FirebaseAuth mAuth;
    private static FirebaseStorage mStorage;
    private static StorageReference mRefStorage;

    public static DatabaseReference getDatabaseReference(){
        if (mRef == null)
            mRef = FirebaseDatabase.getInstance().getReference();

        return mRef;
    }

    public static FirebaseAuth getFirebaseAuth(){
        if (mAuth == null)
            mAuth = FirebaseAuth.getInstance();

        return mAuth;
    }

    public static FirebaseStorage getFirebaseStorage(){
        if (mStorage == null)
            mStorage = FirebaseStorage.getInstance();

        return mStorage;
    }

    public static StorageReference getFirebaseStorageReference(){
        if (mRefStorage == null){
            mRefStorage = getFirebaseStorage().getReference();
        }

        return mRefStorage;
    }
}
