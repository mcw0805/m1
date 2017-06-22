package com.example.mcw0805.wheres_my_stuff.Controller;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mcw0805.wheres_my_stuff.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Controller for resetting the password if forgotten.
 * Sends an email to the existing email address for reset password instructions.
 *
 * @author Chaewon Min
 */
public class ResetPasswordActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText enteredEmail;
    private Button resetPasswordBtn;
    private Button backBtn;

    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth;

    private static final String TAG = "ResetPasswordActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        enteredEmail = (EditText) findViewById(R.id.email_forgot_pw);
        resetPasswordBtn = (Button) findViewById(R.id.reset_pw_btn);
        backBtn = (Button) findViewById(R.id.back_btn_forgot_pw);

        backBtn.setOnClickListener(this);
        resetPasswordBtn.setOnClickListener(this);

        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View v) {
        if (v == backBtn) {
            finish();
            startActivity(new Intent(this, LogInActivity.class));
        }

        if (v == resetPasswordBtn) {
            resetPassword();
        }
    }

    private void resetPassword() {

        String email = enteredEmail.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please enter your email id.", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("Email sending for password reset instructions");
        progressDialog.show();
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
//                    Toast.makeText(getApplicationContext(),
//                            "Email sent for password reset instructions", Toast.LENGTH_SHORT).show();


                    Log.d(TAG, "User password updated.");
                    startActivity(new Intent(getApplicationContext(), LogInActivity.class));
                    progressDialog.dismiss();

                } else {
                    Toast.makeText(getApplicationContext(),
                            "This email does not exist. Failed to send email.", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "User password not updated.");
                    startActivity(new Intent(getApplicationContext(), ResetPasswordActivity.class));
                }
            }
        });

    }

}
