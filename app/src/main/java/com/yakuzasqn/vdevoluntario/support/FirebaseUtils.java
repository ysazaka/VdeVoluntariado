package com.yakuzasqn.vdevoluntario.support;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.yakuzasqn.vdevoluntario.model.User;

/**
 * Created by yoshi on 21/04/2018.
 */

public class FirebaseUtils {
    private static DatabaseReference mRef;
    private static FirebaseAuth mAuth;
    private static FirebaseStorage mStorage;
    private static StorageReference mRefStorage;

    public static DatabaseReference getBaseRef(){
        if (mRef == null)
            mRef = FirebaseDatabase.getInstance().getReference();

        return mRef;
    }

    public static DatabaseReference getUsersRef() {
        return getBaseRef().child("users");
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

    public static String getCurrentUserId() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            return user.getUid();
        }
        return null;
    }

    public static User getActualUser() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) return null;
        return new User(user.getUid(), user.getDisplayName(), user.getPhotoUrl().toString());
    }

//    public static String getPeoplePath() {
//        return "people/";
//    }
//

}
