package com.example.mcw0805.wheres_my_stuff.Controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.mcw0805.wheres_my_stuff.R;

public class Dashboard extends AppCompatActivity implements View.OnClickListener {

    private TextView lostNearMe;
    private TextView foundNearMe;
    private TextView newFound;
    private TextView newLost;
    private TextView request;
    private TextView donate;
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

        lostNearMe.setOnClickListener(this);
        foundNearMe.setOnClickListener(this);
        newFound.setOnClickListener(this);
        newLost.setOnClickListener(this);
        request.setOnClickListener(this);
        donate.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == newLost) {
            Intent intent = new Intent(this, LostItemFormActivity.class);
            Dashboard.this.startActivity(intent);
        }
    }
}
