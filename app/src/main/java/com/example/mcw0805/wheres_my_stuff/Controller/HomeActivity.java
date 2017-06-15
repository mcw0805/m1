package com.example.mcw0805.wheres_my_stuff.Controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.mcw0805.wheres_my_stuff.R;

public class HomeActivity extends AppCompatActivity {

    //create the buttons on the home page
    private Button loginHome;
    private Button registerHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        loginHome = (Button) findViewById(R.id.loginHome);
        registerHome = (Button) findViewById(R.id.registerHome);

        loginHome.setOnClickListener(new View.OnClickListener() {
            public void onClick(View V) {
                Intent intent = new Intent (HomeActivity.this, LogInActivity.class);
                HomeActivity.this.startActivity(intent);
            }
        });

    }

}
