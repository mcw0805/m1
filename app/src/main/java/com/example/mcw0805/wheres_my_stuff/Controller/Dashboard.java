package com.example.mcw0805.wheres_my_stuff.Controller;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.example.mcw0805.wheres_my_stuff.R;
/* Created by Chianne Connelly
* version 1.0
 */

public class Dashboard extends AppCompatActivity implements View.OnClickListener {

    /*
    * textviews/button for the various textfields/button on the dashboard
     */
    private TextView welcome;
    private TextView lostNearMe;
    private TextView foundNearMe;
    private TextView submitted_items;
    private TextView newLost;
    private TextView donate_list;
    private TextView donate;
    private TextView profile_page;
    private Button logout_dash;

    /*
    * variables that will belong to each particular object
     */
    private String currentUserId;
    private String name;
    private String email;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private boolean isAuthListenerSet = false;

    private final String TAG = "Dashboard";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        /*
        * sets all textviews in the view to the instances in the controller
         */
        lostNearMe = (TextView) findViewById(R.id.lost_txt);
        foundNearMe = (TextView) findViewById(R.id.found_txt);
        submitted_items = (TextView) findViewById(R.id.reportFound_txt);
        newLost = (TextView) findViewById(R.id.reportLost_txt);
        donate_list = (TextView) findViewById(R.id.donate_list);
        donate = (TextView) findViewById(R.id.donate_txt);
        profile_page = (TextView) findViewById(R.id.profile_txt);
        welcome = (TextView) findViewById(R.id.welcome);
        logout_dash = (Button) findViewById(R.id.logout_button);
        //welcome.setText("Welcome " + name);

        /*
        * tells the clickListener that an action will occur in this class
         */
        lostNearMe.setOnClickListener(this);
        foundNearMe.setOnClickListener(this);
        submitted_items.setOnClickListener(this);
        newLost.setOnClickListener(this);
        donate_list.setOnClickListener(this);
        donate.setOnClickListener(this);
        profile_page.setOnClickListener(this);
        logout_dash.setOnClickListener(this);

        //connecting the user who just signed in with the dashboard that pops up
        Intent intent = getIntent();
        currentUserId = intent.getStringExtra("currentUserId");
        name = intent.getStringExtra("name");
        email = intent.getStringExtra("email");

        mAuth = FirebaseAuth.getInstance();


        //Firebase authorization
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

        //welcome.setText("Welcome " + email);
        welcome.setText("Welcome " );


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

    /*
    * navigates the user to the next page depending on which item they select
     */

    @Override
    public void onClick(View v) {
        if (v.equals(newLost)) {
            Intent intent = new Intent(this, SubmitFormActivity.class);
            Dashboard.this.startActivity(intent);
        } else if (v.equals(lostNearMe)) {
            Intent intent = new Intent(this, LostItemListActivity.class);
            Dashboard.this.startActivity(intent);
        } else if (v.equals(profile_page)) {
            Intent intent = new Intent(this, ProfileActivity.class);
            Dashboard.this.startActivity(intent);
        } else if (v.equals(foundNearMe)) {
            Intent intent = new Intent(this, FoundItemListActivity.class);
            Dashboard.this.startActivity(intent);
        } else if (v.equals(donate)) {
            Intent intent = new Intent(this, DonateItemFormActivity.class);
            Dashboard.this.startActivity(intent);
        } else if (v.equals(logout_dash)) {
            signOut();
            Toast.makeText(getApplicationContext(),
                    "Successfully signed out.", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(Dashboard.this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();

        } else if (v.equals(submitted_items)) {
            Intent intent = new Intent(this, MyListActivity.class);
            Dashboard.this.startActivity(intent);
        }
        /* else if (v.equals(donate_list)) {
            Intent intent = new Intent(this, someActivity.class;
            Dashboard.this.startActivity(intent);
         */
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
        Log.d(TAG, "signed out");
    }
}
