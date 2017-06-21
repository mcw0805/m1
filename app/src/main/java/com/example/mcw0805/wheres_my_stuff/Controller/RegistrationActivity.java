package com.example.mcw0805.wheres_my_stuff.Controller;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.example.mcw0805.wheres_my_stuff.R;
import com.google.firebase.auth.FirebaseAuth;

/**
 * @Author Jordan Taylor
 * @Version 1.0
 */
public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener {

    private Button cancelButton, loginButton;
    private EditText name, email, password;
    private Switch admin;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        mAuth = FirebaseAuth.getInstance();
        cancelButton = (Button) findViewById(R.id.registrationCancel);
        cancelButton.setOnClickListener(this);

        loginButton = (Button) findViewById(R.id.registrationSubmit);
        loginButton.setOnClickListener(this);

        name = (EditText) findViewById(R.id.registrationName);
        email = (EditText) findViewById(R.id.registrationEmail);
        password = (EditText) findViewById(R.id.registrationPassword);
        admin = (Switch) findViewById(R.id.registrationAdmin);
    }

    @Override
    public void onClick(View v) {

        String inputName = name.getText().toString();
        String inputEmail = email.getText().toString();
        String inputPassword = password.getText().toString();
        boolean inputAdmin = admin.isChecked();

        if (v == loginButton) {
            if (inputEmail.length() == 0 || inputName.length() == 0 ||
                    inputPassword.length() == 0) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
                builder1.setMessage("Please fill out all required fields.");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert = builder1.create();
                alert.show();
                return;
            }
            finish();
            startActivity(new Intent(this, HomeActivity.class));
        }

        if (v == cancelButton) {
            finish();
            startActivity(new Intent(this, HomeActivity.class));
        }
    }

}
