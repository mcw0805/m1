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

    private TextView lostNearMe;
    private TextView foundNearMe;
    private TextView newFound;
    private TextView newLost;
    private TextView request;
    private TextView donate;
    private TextView profile_page;

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

        lostNearMe.setOnClickListener(this);
        foundNearMe.setOnClickListener(this);
        newFound.setOnClickListener(this);
        newLost.setOnClickListener(this);
        request.setOnClickListener(this);
        donate.setOnClickListener(this);
        profile_page.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.equals(newLost)) {
            Intent intent = new Intent(this, LostItemFormActivity.class);
            Dashboard.this.startActivity(intent);
        } else if (v.equals(lostNearMe)) {
            Intent intent = new Intent(this, LostItemList.class);
            Dashboard.this.startActivity(intent);
        } else if (v.equals(profile_page)) {
            Intent intent = new Intent(this, ProfileActivity.class);
            Dashboard.this.startActivity(intent);
        }
    }
}
