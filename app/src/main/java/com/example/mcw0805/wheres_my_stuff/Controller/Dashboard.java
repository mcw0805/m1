package com.example.mcw0805.wheres_my_stuff.Controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.mcw0805.wheres_my_stuff.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
/* Created by Chianne Connelly
* version 1.0
 */

public class Dashboard extends AppCompatActivity implements View.OnClickListener {

/*
* textviews for the various textfields on the dashboard
 */
    private TextView welcome;
    private TextView lostNearMe;
    private TextView foundNearMe;
    private TextView newFound;
    private TextView newLost;
    private TextView request;
    private TextView donate;
    private TextView profile_page;

    /*
    * variables that will belong to each particular object
     */
    private String currentUserId;
    private String name;
    private String email;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard2);
        /*
        * sets all textviews in the view to the instances in the controller
         */
        lostNearMe = (TextView) findViewById(R.id.lost_txt);
        foundNearMe = (TextView) findViewById(R.id.found_txt);
        newFound = (TextView) findViewById(R.id.reportFound_txt);
        newLost = (TextView) findViewById(R.id.reportLost_txt);
        request = (TextView) findViewById(R.id.request_txt);
        donate = (TextView) findViewById(R.id.donate_txt);
        profile_page = (TextView) findViewById(R.id.profile_txt);
        welcome = (TextView) findViewById(R.id.welcome);
        //welcome.setText("Welcome " + name);

        /*
        * tells the clickListener that an action will occur in this class
         */
        lostNearMe.setOnClickListener(this);
        foundNearMe.setOnClickListener(this);
        newFound.setOnClickListener(this);
        newLost.setOnClickListener(this);
        request.setOnClickListener(this);
        donate.setOnClickListener(this);
        profile_page.setOnClickListener(this);

        //connecting the user who just signed in with the dashboard that pops up
        Intent intent = getIntent();
        currentUserId = intent.getStringExtra("currentUserId");
        name = intent.getStringExtra("name");
        email = intent.getStringExtra("email");

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        String email = null;
        if (user != null) {
            email = user.getEmail();
        }

        //welcome.setText("Welcome " + email);
        welcome.setText("Welcome " + name);


    }

    /*
    * navigates the user to the next page depending on which item they select
     */

    @Override
    public void onClick(View v) {
        if (v.equals(newLost)) {
            Intent intent = new Intent(this, LostItemFormActivity.class);
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
        } else if (v.equals(request)) {
            Intent intent = new Intent(this, RequestItemFormActivity.class);
            Dashboard.this.startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
