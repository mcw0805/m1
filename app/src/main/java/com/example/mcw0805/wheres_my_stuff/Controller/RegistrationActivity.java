package com.example.mcw0805.wheres_my_stuff.Controller;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.example.mcw0805.wheres_my_stuff.Model.User;
import com.example.mcw0805.wheres_my_stuff.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

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
    private ProgressDialog progressDialog;



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
        progressDialog = new ProgressDialog(this);
    }

    @Override
    public void onClick(View v) {

        final String inputName = name.getText().toString();
        final String inputEmail = email.getText().toString();
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
            progressDialog.setMessage("Signing in...");
            progressDialog.show();
            mAuth.createUserWithEmailAndPassword(inputEmail, inputPassword).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    progressDialog.dismiss();

                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        FirebaseUser user = mAuth.getCurrentUser();
                        String id = user.getUid();
                        User u = new User(inputName, inputEmail, id);
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference userRef = database.getReference("users");
                        Map<String, JSONObject> userMap= new HashMap<String, JSONObject>();
                        JSONObject tempObject = new JSONObject();
                        try {
                            tempObject.put("Name","inputName");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            tempObject.put("Email","inputEmail");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        userMap.put("UID", tempObject);
                        userRef.setValue(userMap);
                        Intent intent = new Intent(RegistrationActivity.this, Dashboard.class);
                        intent.putExtra("currentUserId", id);
                        intent.putExtra("name", inputName);
                        intent.putExtra("email", inputEmail);
                        RegistrationActivity.this.startActivity(intent);
                    } else {
                        // If sign in fails, display a message to the user.
                        Exception e = task.getException();
                        if (e instanceof FirebaseAuthWeakPasswordException) {
                            AlertDialog.Builder builder1 = new AlertDialog.Builder(RegistrationActivity.this);
                            builder1.setMessage(((FirebaseAuthWeakPasswordException)e).getReason());
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
                        } else if (e instanceof FirebaseAuthInvalidCredentialsException) {
                            AlertDialog.Builder builder1 = new AlertDialog.Builder(RegistrationActivity.this);
                            builder1.setMessage("Please enter a valid email");
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
                            Log.d("Invalid Credentials","Bad Email");
                        } else if (e instanceof FirebaseAuthInvalidCredentialsException) {
                            AlertDialog.Builder builder1 = new AlertDialog.Builder(RegistrationActivity.this);
                            builder1.setMessage("Oops!\nIt looks like there is already an account associated with"
                                    + "that email.");
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
                        } else {
                            AlertDialog.Builder builder1 = new AlertDialog.Builder(RegistrationActivity.this);
                            builder1.setMessage("Registration Failed. Try again later.");
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
                        }
                    }
                }
            });
        }

        if (v == cancelButton) {
            finish();
            startActivity(new Intent(this, HomeActivity.class));
        }
    }
//    public void register(String user, String password) {
//        progressDialog.setMessage("Signing in...");
//        progressDialog.show();
//            mAuth.createUserWithEmailAndPassword(user, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                @Override
//                public void onComplete(@NonNull Task<AuthResult> task) {
//                    progressDialog.dismiss();
//
//                    if (task.isSuccessful()) {
//                        // Sign in success, update UI with the signed-in user's information
//                        FirebaseUser user = mAuth.getCurrentUser();
//                        Intent intent = new Intent(LogInActivity.this, Dashboard.class);
//                        LogInActivity.this.startActivity(intent);
//                    } else {
//                        // If sign in fails, display a message to the user.
//                        Exception e = task.getException();
//                        if (e.equals(FirebaseAuthWeakPasswordException)) {
//                            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
//                            builder1.setMessage(e.getReason());
//                            builder1.setCancelable(true);
//                            builder1.setPositiveButton(
//                                    "Ok",
//                                    new DialogInterface.OnClickListener() {
//                                        public void onClick(DialogInterface dialog, int id) {
//                                            dialog.cancel();
//                                        }
//                                    });
//                            AlertDialog alert = builder1.create();
//                            alert.show();
//                        } else if (e.equals(FirebaseAuthInvalidCredentialsException)) {
//                            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
//                            builder1.setMessage("Please enter a valid email");
//                            builder1.setCancelable(true);
//                            builder1.setPositiveButton(
//                                    "Ok",
//                                    new DialogInterface.OnClickListener() {
//                                        public void onClick(DialogInterface dialog, int id) {
//                                            dialog.cancel();
//                                        }
//                                    });
//                            AlertDialog alert = builder1.create();
//                            alert.show();
//                        } else if (e.equals(FirebaseAuthInvalidCredentialsException)) {
//                            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
//                            builder1.setMessage("Oops!\nIt looks like there is already an account associated with"
//                                    + "that email.");
//                            builder1.setCancelable(true);
//                            builder1.setPositiveButton(
//                                    "Ok",
//                                    new DialogInterface.OnClickListener() {
//                                        public void onClick(DialogInterface dialog, int id) {
//                                            dialog.cancel();
//                                        }
//                                    });
//                            AlertDialog alert = builder1.create();
//                            alert.show();
//                        } else {
//                            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
//                            builder1.setMessage("Registration Failed. Try again later.");
//                            builder1.setCancelable(true);
//                            builder1.setPositiveButton(
//                                    "Ok",
//                                    new DialogInterface.OnClickListener() {
//                                        public void onClick(DialogInterface dialog, int id) {
//                                            dialog.cancel();
//                                        }
//                                    });
//                            AlertDialog alert = builder1.create();
//                            alert.show();
//                        }
//                    }
//                }
//            });
//    }
}
