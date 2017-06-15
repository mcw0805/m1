package com.example.mcw0805.wheres_my_stuff.Controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.mcw0805.wheres_my_stuff.R;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener {

    private Button cancelButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        cancelButton = (Button) findViewById(R.id.registrationCancel);

        cancelButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == cancelButton) {
            finish();
            startActivity(new Intent(this, HomeActivity.class));
        }

    }
}
