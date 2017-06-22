package com.example.mcw0805.wheres_my_stuff.Controller;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

import com.example.mcw0805.wheres_my_stuff.R;
import com.google.firebase.auth.FirebaseUser;

/**
 * @author Melanie Hall
 */

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener  {

    //Log out button.
    private Button signOutProfileButton;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private boolean isAuthListenerSet = false;
    private static final String TAG = "ProfileActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        signOutProfileButton = (Button) findViewById(R.id.profile_logout_btn);

        signOutProfileButton.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!isAuthListenerSet) {
            FirebaseAuth.getInstance().addAuthStateListener(mAuthListener);
            isAuthListenerSet = true;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            FirebaseAuth.getInstance().removeAuthStateListener(mAuthListener);
            isAuthListenerSet = false;
        }
    }

    @Override
    public void onClick(View view) {
        if (view == signOutProfileButton) {
            finish();
            signOut();
            Intent intent = new Intent(ProfileActivity.this, HomeActivity.class);
            ProfileActivity.this.startActivity(intent);

        }

    }

    private void signOut() {
        mAuth.signOut();
        Log.d(TAG, "signed out");
    }
}
