package com.example.mcw0805.wheres_my_stuff.Controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mcw0805.wheres_my_stuff.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * @Author Ted Shang
 * @Version 1.1
 * Removed pending and approved categories as of 7/11/17
 */
public class AdminDashboard extends AppCompatActivity implements View.OnClickListener {
    //private TextView pending;
    //private TextView approved;
    private TextView active;
    private TextView banned;
    private TextView locked;
    private TextView profile;
    private Button logout;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private boolean isAuthListenerSet = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Set all text views
        setContentView(R.layout.activity_admin_dashboard);
        //pending = (TextView) findViewById(R.id.pendingitems);
        //approved = (TextView) findViewById(R.id.approveditems);
        active = (TextView) findViewById(R.id.activeusers);
        banned = (TextView) findViewById(R.id.bannedusers);
        locked = (TextView) findViewById(R.id.lockedusers);
        profile = (TextView) findViewById(R.id.profileadmin);
        logout = (Button) findViewById(R.id.admin_logout);

        //Creates all listeners
        //pending.setOnClickListener(this);
        //approved.setOnClickListener(this);
        active.setOnClickListener(this);
        banned.setOnClickListener(this);
        locked.setOnClickListener(this);
        profile.setOnClickListener(this);
        logout.setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();

        //Firebase authorization
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
            }
        };

    }

    @Override
    public void onClick(View v) {
        /*//if (v.equals(pending)) {
            Intent intent = new Intent (this, PendingItemListActivity.class);
            AdminDashboard.this.startActivity(intent);
        }
        //else if (v.equals(approved)) {
            Intent intent = new Intent (this, ApprovedItemListActivity.class);
            AdminDashboard.this.startActivity(intent);
        }*/
        if (v.equals(active)) {
            Intent intent = new Intent(this, ActiveUserListActivity.class);
            AdminDashboard.this.startActivity(intent);
        } else if (v.equals(banned)) {
            Intent intent = new Intent(this, BannedUserListActivity.class);
            AdminDashboard.this.startActivity(intent);
        } else if (v.equals(locked)) {
            Intent intent = new Intent(this, LockedUserListActivity.class);
            AdminDashboard.this.startActivity(intent);
        } else if (v.equals(profile)) {
            Intent intent = new Intent(this, ProfileActivity.class);
            AdminDashboard.this.startActivity(intent);
        } else if (v.equals(logout)) {
            signOut();
            Toast.makeText(getApplicationContext(),
                    "Successfully signed out.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(AdminDashboard.this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        if (!isAuthListenerSet) {
            mAuth.addAuthStateListener(mAuthListener);
            isAuthListenerSet = true;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAuth.addAuthStateListener(mAuthListener);
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
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    /**
     * Method that signs a user out.
     */
    private void signOut() {
        mAuth.signOut();
    }

}
