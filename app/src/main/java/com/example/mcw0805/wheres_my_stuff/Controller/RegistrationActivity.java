package com.example.mcw0805.wheres_my_stuff.Controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import com.example.mcw0805.wheres_my_stuff.R;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener {

    private Button cancelButton, loginButton;
    private EditText name, email, password;
    private Switch admin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        cancelButton = (Button) findViewById(R.id.registrationCancel);
        cancelButton.setOnClickListener(this);

        loginButton = (Button) findViewById(R.id.registrationSubmit);
        loginButton.setOnClickListener(this);

        name = (EditText) findViewById(R.id.registrationName);
        email = (EditText) findViewById(R.id.registrationEmail);
        password = (EditText) findViewById(R.id.registrationPassword);
        admin = (Switch) findViewById(R.id.registrationAdmin);
//
//        try {
//
//        } catch

    }

    @Override
    public void onClick(View v) {
        int i = 0;
        if (v == cancelButton) {
            i = 1;
        } else if (v == loginButton) {
            i = 2;
        }
        switch (i) {
            case 1:
                finish();
                startActivity(new Intent(this, HomeActivity.class));
                break;
            case 2:
                finish();
                startActivity(new Intent(this, ProfileActivity.class));
                break;
            default:
                break;
        }
    }

}
