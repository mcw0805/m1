package com.example.mcw0805.wheres_my_stuff.Controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.mcw0805.wheres_my_stuff.R;
/* Created by Chianne Connelly
* version 1.0
 */

public class Dashboard extends AppCompatActivity implements View.OnClickListener {

    private TextView welcome;
    private TextView lostNearMe;
    private TextView foundNearMe;
    private TextView newFound;
    private TextView newLost;
    private TextView request;
    private TextView donate;
    private TextView profile_page;
    private String currentUserId;
    private String name;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard2);

        lostNearMe = (TextView) findViewById(R.id.lost_txt);
        foundNearMe = (TextView) findViewById(R.id.found_txt);
        newFound = (TextView) findViewById(R.id.reportFound_txt);
        newLost = (TextView) findViewById(R.id.reportLost_txt);
        request = (TextView) findViewById(R.id.request_txt);
        donate = (TextView) findViewById(R.id.donate_txt);
        profile_page = (TextView) findViewById(R.id.profile_txt);
        welcome = (TextView) findViewById(R.id.welcome);
        welcome.setText("Welcome " + name);

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
    }

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
        }
    }
}
