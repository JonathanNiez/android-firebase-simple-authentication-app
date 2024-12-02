package com.example.authapp.utility;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirebaseHelper {
    private static FirebaseAuth mAuth;
    private static FirebaseUser currentUser;
    private static FirebaseFirestore fStore;

    public static FirebaseAuth getAuth() {
        if (mAuth == null) {
            mAuth = FirebaseAuth.getInstance();
        }
        return mAuth;
    }

    public static FirebaseUser getCurrentUser() {
        if (currentUser == null) {
            currentUser = getAuth().getCurrentUser();
        }
        return currentUser;
    }

    public static FirebaseFirestore getFireStoreInstance() {
        if (fStore == null) {
            fStore = FirebaseFirestore.getInstance();
        }
        return fStore;
    }

    public static void signOutUser() {
        getAuth().signOut();
    }
}
