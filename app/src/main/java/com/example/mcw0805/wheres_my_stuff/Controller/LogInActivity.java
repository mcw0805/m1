package com.example.mcw0805.wheres_my_stuff.Controller;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.view.View;


import com.example.mcw0805.wheres_my_stuff.R;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * @Author Ted Shang
 * @Version 1.0
 */
public class LogInActivity extends AppCompatActivity  {

    //We will be getting information from the following widgets:

    private EditText loginUsername;
    private EditText loginPassword;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        loginUsername =(EditText) findViewById(R.id.login_username_edit);
        loginPassword =(EditText) findViewById(R.id.login_pw_edit);

        //Create the buttons
        Button logIn = (Button) findViewById(R.id.login_btn);
        Button back = (Button) findViewById(R.id.login_back_btn);

        logIn.setOnClickListener(new View.OnClickListener() {
            //Gets the information from the EditTexts and checks it against a username/password
            //database. Either displays incorrect password or advances the scene
            public void onClick(View v) {
                String username = loginUsername.getText().toString();
                String password = loginPassword.getText().toString();
                //Temporary hardcode
                String hardUsername = "user";
                String hardPassword = "pass";
                if (username.equals(hardUsername) && password.equals(hardPassword)) {
                    //GOTO application
                    Intent intent = new Intent(LogInActivity.this, ProfileActivity.class);
                    LogInActivity.this.startActivity(intent);
                } else {
                    // Inform user of bad login attempt
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            //Goes back to the main splash screen
            public void onClick(View V) {
                Intent intent = new Intent (LogInActivity.this, HomeActivity.class);
                LogInActivity.this.startActivity(intent);
            }
        });


    }

}
