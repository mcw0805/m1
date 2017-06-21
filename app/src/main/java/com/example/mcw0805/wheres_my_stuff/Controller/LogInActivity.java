package com.example.mcw0805.wheres_my_stuff.Controller;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.view.View;
import android.widget.Toast;


import com.example.mcw0805.wheres_my_stuff.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * @Author Ted Shang
 * @Version 1.0
 */
public class LogInActivity extends AppCompatActivity implements View.OnClickListener {

    //We will be getting information from the following widgets:
    //Note username current is the same as email as of 6/20/2017

    private EditText loginUsername;
    private EditText loginPassword;
    private Button login;
    private Button back;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private static final String TAG = "LoginActivity";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };

        loginUsername = (EditText) findViewById(R.id.login_username_edit);
        loginPassword = (EditText) findViewById(R.id.login_pw_edit);

        //set the buttons
        login = (Button) findViewById(R.id.login_btn);
        back = (Button) findViewById(R.id.login_back_btn);
        login.setOnClickListener(this);
        back.setOnClickListener(this);

    }
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }


    @Override
    public void onClick(View v) {

        if (v == login) {
            //Gets the information from the EditTexts and checks it against a username/password
            //database. Either displays incorrect password or advances the scene
            String username = loginUsername.getText().toString();
            String password = loginPassword.getText().toString();
            //check to see if fields are valid first;
            if (username.length() == 0 || password.length() == 0) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
                builder1.setMessage("Please fill out both required fields.");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                builder1.setNegativeButton(
                        "Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();
                return;
            }
            mAuth.signInWithEmailAndPassword(username, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                Intent intent = new Intent(LogInActivity.this, Dashboard.class);
                                LogInActivity.this.startActivity(intent);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(LogInActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                            /*Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());
                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            if (!task.isSuccessful()) {
                                Log.w(TAG, "signInWithEmail:failed", task.getException());
                                Toast.makeText(LogInActivity.this,"Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                            Intent intent = new Intent(LogInActivity.this, Dashboard.class);
                            LogInActivity.this.startActivity(intent);

                            // ...*/
                        }
                    });
            /*//Temporary hardcode
            String hardUsername = "user";
            String hardPassword = "pass";
            if (username.equals(hardUsername) && password.equals(hardPassword)) {
                //GOTO application1
                Toast.makeText(this, "Successful login",
                        Toast.LENGTH_LONG).show();
                Intent intent = new Intent(LogInActivity.this, Dashboard.class);
                LogInActivity.this.startActivity(intent);*/
            /*} else {
                // Inform user of bad login attempt
                AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
                builder1.setMessage("Invalid Username/Password Combination");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                builder1.setNegativeButton(
                        "Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();*/


            }
        if (v == back) {
            //Goes back to the main splash screen
            finish();
            Intent intent = new Intent (LogInActivity.this, HomeActivity.class);
            LogInActivity.this.startActivity(intent);
        }

    }
}
