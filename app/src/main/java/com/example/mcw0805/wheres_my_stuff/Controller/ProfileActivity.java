package com.example.mcw0805.wheres_my_stuff.Controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;
import android.view.View.OnClickListener;

import com.example.mcw0805.wheres_my_stuff.R;

/**
 * Author Melanie Hall
 */

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener  {

    //Log out button.
    private Button logoutProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        logoutProfile = (Button) findViewById(R.id.profile_logout_btn);

        logoutProfile.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == logoutProfile) {
            Intent intent = new Intent(ProfileActivity.this, HomeActivity.class);
            ProfileActivity.this.startActivity(intent);
        }
    }
}
