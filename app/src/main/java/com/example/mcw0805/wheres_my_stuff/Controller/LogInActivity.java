package com.example.mcw0805.wheres_my_stuff.Controller;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationSet;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mcw0805.wheres_my_stuff.Controller.AdminFunctionality.AdminDashboard;
import com.example.mcw0805.wheres_my_stuff.Model.User;
import com.example.mcw0805.wheres_my_stuff.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Controller for the login. Uses Firebase email password authentication.
 *
 * @author Ted Shang
 * @version 1.3
 */
public class LogInActivity extends AppCompatActivity implements View.OnClickListener {

    //We will be getting information from the following widgets:
    //Note username current is the same as email as of 6/20/2017

    private EditText loginUsername;
    private EditText loginPassword;
    private Button login;
    private Button back;
    private TextView forgotPassword;
    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private static final String TAG = "LoginActivity";
    private DatabaseReference mUserRef;
    private DatabaseReference mUserRef2;
    //transition instance data:
    private AnimationSet set;


    /**
     * Creates the activity and instantiates widgets
     * @param savedInstanceState saved instance
     */
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
            }
        };

        loginUsername = (EditText) findViewById(R.id.login_username_edit);
        loginPassword = (EditText) findViewById(R.id.login_pw_edit);
        forgotPassword = (TextView) findViewById(R.id.forgot_pw);

        //set the buttons
        login = (Button) findViewById(R.id.login_btn);
        back = (Button) findViewById(R.id.login_back_btn);
        login.setOnClickListener(this);
        back.setOnClickListener(this);
        forgotPassword.setOnClickListener(this);

        progressDialog = new ProgressDialog(this);

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
            login();

        }

        if (v == back) {
            //Goes back to the main splash screen
            Intent intent = new Intent(LogInActivity.this, HomeActivity.class);
            LogInActivity.this.startActivity(intent);
            finish();
        }

        if (v == forgotPassword) {
            startActivity(new Intent(this, ResetPasswordActivity.class));
            finish();
        }

    }

    /**
     * The method that verifies user logins and checks for valid information. Valid username/pass
     * combinations will lead to the next activity
     */
    private void login() {
        //Gets the information from the EditTexts and checks it against a username/password
        //database. Either displays incorrect password or advances the scene
        final String username = loginUsername.getText().toString();
        String password = loginPassword.getText().toString();

        if (TextUtils.isEmpty(username)) {
            //email is empty
            Log.d(TAG, "email is empty");

            Toast.makeText(this, "Please enter an email address", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "toast created");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            //password is empty
            Log.d(TAG, "password is empty");
            Toast.makeText(this, "Please enter a password", Toast.LENGTH_SHORT).show();
            return;
        }
        progressDialog.setMessage("Signing in...");
        progressDialog.show();

        mAuth.signInWithEmailAndPassword(username, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();

                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            assert user != null;
                            final String uid = user.getUid();
                            mUserRef = FirebaseDatabase.getInstance().getReference("users");
                            mUserRef2 = mUserRef.child(uid);
                            mUserRef.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for (DataSnapshot user : dataSnapshot.getChildren()) {
                                        User u = User.buildUserObject(user);
                                        if (u != null) {
                                            Log.d(TAG, Boolean.toString(u.getIsBanned()));
                                            if (u.getUid().equals(uid)) {
                                                if (u.getIsBanned()) {
                                                    //If banned ALERT
                                                    bannedUser();
                                                    return;
                                                    //Checks if user is locked out
                                                } else if (u.getLockAttempts() > 2) {
                                                    lockedUser();
                                                    return;
                                                } else {
                                                    //Everything is fine
                                                    //Reset login attempts
                                                    DatabaseReference mLoginAttempts = mUserRef2.
                                                            child("lockAttempts");
                                                    mLoginAttempts.setValue(0);
                                                    //advance to next screen

                                                    Intent intent = new Intent(LogInActivity.
                                                            this, Dashboard.class);
                                                    LogInActivity.this.startActivity(intent);
                                                    overridePendingTransition(R.transition.fade_in,
                                                            R.transition.fade_out);
                                                    finish();
                                                    return;
                                                }
                                            }
                                        }
                                    }
                                    //otherwise we have an admin and can proceed to AdminDashboard
                                    Intent intent = new Intent(LogInActivity.this,
                                            AdminDashboard.class);
                                    LogInActivity.this.startActivity(intent);
                                    finish();

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LogInActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //If user exists, add an incorrect login attempt
                            DatabaseReference allUsers = FirebaseDatabase.getInstance().
                                    getReference("users");
                            allUsers.addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                    for (DataSnapshot datasnap : dataSnapshot.getChildren()) {
                                        User u = User.buildUserObject(dataSnapshot);
                                        if (u.getEmail().equals(username)) {
                                            u.addLockAttempts();
                                            u.writeToDatabase();
                                            if (u.getLockAttempts() > 2) {
                                                lockedUser();
                                                return;
                                            }
                                        }

                                    }
                                }

                                @Override
                                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                                }

                                @Override
                                public void onChildRemoved(DataSnapshot dataSnapshot) {

                                }

                                @Override
                                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                    }
                });

    }

    /**
     * Logic if user is banned
     */
    private void bannedUser() {
        Log.d(TAG, "signInWithEmail:banned");
        AlertDialog.Builder builder1 =
                new AlertDialog.Builder(LogInActivity.
                        this);
        builder1.setMessage("Your account"
                + "has been BANNED for violating ToS. "
                + "Please contact support ");
        builder1.setCancelable(true);
        builder1.setPositiveButton(
                "Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        finish();
                        startActivity(getIntent());
                    }
                });

        builder1.setNegativeButton(
                "Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        finish();
                        startActivity(getIntent());
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
        return;
    }

    /**
     * Logic if user is locked
     */
    private void lockedUser() {
        //locked out set value to true
        DatabaseReference mLockedout = mUserRef2.
                child("locked");
        mLockedout.setValue(true);
        //if locked out ALERT
        AlertDialog.Builder builder1 = new AlertDialog.
                Builder(LogInActivity.this);
        builder1.setMessage("Your account has been"
                + " locked from too many incorrect"
                + " logins. Please try later");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        finish();
                        startActivity(getIntent());
                    }
                });

        builder1.setNegativeButton(
                "Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        finish();
                        startActivity(getIntent());
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
        return;
    }

    /**
     * tests banned users without log and alert for testing purposes
     * @param user a user
     * @return true or false if operation is succesful
     */
    public boolean testBan(User user) {
        if (user.getIsBanned()) {
            return true;
        }
        return false;
    }
    /**
     * tests locked users without log and alert for testing purposes
     * @param user a user
     * @return true or false if operation is successful
     */
    public boolean testLock(User user) {
        if (user.getIsLocked()) {
            return true;
        }
        return false;
    }
}