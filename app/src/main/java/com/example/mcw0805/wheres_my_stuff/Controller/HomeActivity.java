package com.example.mcw0805.wheres_my_stuff.Controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ViewFlipper;

import com.example.mcw0805.wheres_my_stuff.R;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    //create the buttons on the home page
    private Button loginHome;
    private Button registerHome;

    private Animation fadeIn;
    private Animation fadeOut;
    private ViewFlipper viewFlipper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        loginHome = (Button) findViewById(R.id.loginHome);
        registerHome = (Button) findViewById(R.id.registerHome);

        loginHome.setOnClickListener(this);
        registerHome.setOnClickListener(this);

        viewFlipper = (ViewFlipper) this.findViewById(R.id.backgroundViewFlipper);
        fadeIn = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
        fadeOut = AnimationUtils.loadAnimation(this, android.R.anim.fade_out);

        viewFlipper.setInAnimation(fadeIn);
        viewFlipper.setOutAnimation(fadeOut);

        //Sets auto flipping.
        viewFlipper.setAutoStart(true);
        viewFlipper.setFlipInterval(5000);
        viewFlipper.startFlipping();
    }

    @Override
    public void onClick(View v) {
        if (v == loginHome) {
            Intent intent = new Intent(HomeActivity.this, LogInActivity.class);
            HomeActivity.this.startActivity(intent);
            finish();
        } else if (v == registerHome) {
            Intent intent = new Intent(HomeActivity.this, RegistrationActivity.class);
            HomeActivity.this.startActivity(intent);
            finish();
        }
    }
}
