package com.example.mcw0805.wheres_my_stuff.Controller;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mcw0805.wheres_my_stuff.Model.User;
import com.example.mcw0805.wheres_my_stuff.R;

/**
 * Class that allows admins to see info about current users and make selected changes;
 * @Author Ted Shang
 * @Version 1.0
 * Model off of ItemDescriptionActivity.java
 */
public class UserDescriptionActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView name;
    private TextView email;
    private TextView itemCount;
    private TextView lockAttempts;
    private TextView locked;
    private TextView banned;
    private TextView uid;
    private CheckBox ban;
    private CheckBox lock;
    private Button confirm;
    private Button cancel;
    private boolean banChange;
    private boolean unbanChange;
    private boolean lockChange;
    private User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_description);
        Intent intent = getIntent();
        user = intent.getParcelableExtra("UserObj");

        // Set all textviews;

        name = (TextView) findViewById(R.id.userOvName);
        email = (TextView) findViewById(R.id.UserOvEmail);
        itemCount = (TextView) findViewById(R.id.UserOvIC);
        lockAttempts = (TextView) findViewById(R.id.userOvLA);
        locked = (TextView) findViewById(R.id.UserOVLS);
        banned = (TextView) findViewById(R.id.userOvBS);
        uid = (TextView) findViewById(R.id.UserOvUID);
        ban = (CheckBox) findViewById(R.id.changeBan);
        lock = (CheckBox) findViewById(R.id.changeLock);
        confirm = (Button) findViewById(R.id.confirmButton);
        cancel = (Button) findViewById(R.id.adminCanel);

        name.setText("" + user.getName());
        email.setText("" + user.getEmail());
        itemCount.setText(Integer.toString(user.getItemCount()));
        lockAttempts.setText(Integer.toString(user.getLockAttempts()));
        locked.setText(String.valueOf(user.getIsLocked()));
        banned.setText(String.valueOf(user.getIsBanned()));
        uid.setText("" + user.getUid());

        confirm.setOnClickListener(this);
        cancel.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v == confirm) {
            String unlocked = "User will be unlocked";
            String unbanned = "User will be unbanned";
            String banned = "User will be banned";
            String mainText = "Are you sure you wish to make changes to this user?";
            if (ban.isChecked() && user.getIsBanned()) {
                mainText = mainText + "\n" + unbanned;
            } else {
                mainText = mainText + "\n" + banned;
            }
            if (lock.isChecked()) {
                mainText = mainText + "\n" + unlocked;
            }
            //Alert admin if they want to make changes
            AlertDialog.Builder builder1 = new AlertDialog.Builder(UserDescriptionActivity.this);
            builder1.setMessage(mainText);
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
                    "Yes",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            finish();
                            if (ban.isChecked()) {
                                if (user.getIsBanned()) {
                                    user.setBanned(false);
                                    unbanChange = true;
                                } else {
                                    user.setBanned(true);
                                    banChange = true;
                                }
                            }
                            if (lock.isChecked()) {
                                if (user.getIsLocked()) {
                                    lockChange = true;
                                    user.setLocked(false);
                                    user.setLockAttempts(0);
                                }
                            }
                            user.writeToDatabase();
                            if (lockChange) {
                                Toast.makeText(getApplicationContext(),
                                        "User has been unlocked", Toast.LENGTH_SHORT).show();
                            }
                            if (banChange) {
                                Toast.makeText(getApplicationContext(),
                                        "User has been banned", Toast.LENGTH_SHORT).show();
                            }
                            if (unbanChange) {
                                Toast.makeText(getApplicationContext(),
                                        "User has been unbanned", Toast.LENGTH_SHORT).show();
                            }
                            Intent intent = new Intent(UserDescriptionActivity.this,
                                    AdminDashboard.class);
                            startActivity(intent);
                        }
                    });

            AlertDialog alert11 = builder1.create();
            alert11.show();
            return;

        } else if (v == cancel) {
            Toast.makeText(getApplicationContext(),
                    "No Changes Made", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(UserDescriptionActivity.this, AdminDashboard.class);
            startActivity(intent);
        }
    }
}
