package com.example.mcw0805.wheres_my_stuff.Controller;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.view.View;
import android.widget.Toast;


import com.example.mcw0805.wheres_my_stuff.R;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * @Author Ted Shang
 * @Version 1.0
 */
public class LogInActivity extends AppCompatActivity implements View.OnClickListener {

    //We will be getting information from the following widgets:

    private EditText loginUsername;
    private EditText loginPassword;
    private Button login;
    private Button back;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        loginUsername = (EditText) findViewById(R.id.login_username_edit);
        loginPassword = (EditText) findViewById(R.id.login_pw_edit);

        //set the buttons
        login = (Button) findViewById(R.id.login_btn);
        back = (Button) findViewById(R.id.login_back_btn);
        login.setOnClickListener(this);
        back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == login) {
            //Gets the information from the EditTexts and checks it against a username/password
            //database. Either displays incorrect password or advances the scene
            String username = loginUsername.getText().toString();
            String password = loginPassword.getText().toString();
            //Temporary hardcode
            String hardUsername = "user";
            String hardPassword = "pass";
            if (username.equals(hardUsername) && password.equals(hardPassword)) {
                //GOTO application1
                Toast.makeText(this, "Successful login",
                        Toast.LENGTH_LONG).show();
                Intent intent = new Intent(LogInActivity.this, ProfileActivity.class);
                LogInActivity.this.startActivity(intent);
            } else {
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
                alert11.show();


            }
        }
        if (v == back) {
            //Goes back to the main splash screen
            Intent intent = new Intent (LogInActivity.this, HomeActivity.class);
            LogInActivity.this.startActivity(intent);
        }

    }
}
