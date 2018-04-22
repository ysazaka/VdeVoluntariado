package com.yakuzasqn.vdevoluntario.support;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by yoshi on 21/04/2018.
 */

public class FirebaseConfig {
    private static DatabaseReference mRef;
    private static FirebaseAuth mAuth;

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
}
