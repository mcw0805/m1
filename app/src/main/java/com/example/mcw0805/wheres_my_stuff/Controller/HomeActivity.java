package com.example.mcw0805.wheres_my_stuff.Controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.example.mcw0805.wheres_my_stuff.R;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    //create the buttons on the home page
    private Button loginHome;
    private Button registerHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        loginHome = (Button) findViewById(R.id.loginHome);
        registerHome = (Button) findViewById(R.id.registerHome);

        loginHome.setOnClickListener(this);
        registerHome.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == loginHome) {
            Intent intent = new Intent(HomeActivity.this, LogInActivity.class);
            HomeActivity.this.startActivity(intent);
            finish();
        } else if (v == registerHome) {
            Intent intent = new Intent (HomeActivity.this, RegistrationActivity.class);
            HomeActivity.this.startActivity(intent);
            finish();
        }
    }
}
