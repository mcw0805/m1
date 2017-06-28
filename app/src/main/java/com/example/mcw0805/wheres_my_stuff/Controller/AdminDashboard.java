package com.example.mcw0805.wheres_my_stuff.Controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.mcw0805.wheres_my_stuff.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.example.mcw0805.wheres_my_stuff.R;

/**
 * @Author Ted Shang
 * @Version 1.0
 */
public class AdminDashboard extends AppCompatActivity implements View.OnClickListener{
    private TextView pending;
    private TextView approved;
    private TextView active;
    private TextView banned;
    private TextView locked;
    private TextView profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Set all text views
        setContentView(R.layout.activity_admin_dashboard);
        pending = (TextView) findViewById(R.id.pendingitems);
        approved = (TextView) findViewById(R.id.approveditems);
        active = (TextView) findViewById(R.id.activeusers);
        banned = (TextView) findViewById(R.id.bannedusers);
        locked = (TextView) findViewById(R.id.lockedusers);
        profile = (TextView) findViewById(R.id.profileadmin);

        //Creates all listeners
        pending.setOnClickListener(this);
        approved.setOnClickListener(this);
        active.setOnClickListener(this);
        banned.setOnClickListener(this);
        locked.setOnClickListener(this);
        profile.setOnClickListener(this);

    }

    @Override
    public void onClick (View v) {
        if (v.equals(pending)) {
            Intent intent = new Intent (this, PendingItemListActivity.class);
            AdminDashboard.this.startActivity(intent);
        }
        else if (v.equals(approved)) {
            Intent intent = new Intent (this, ApprovedItemListActivity.class);
            AdminDashboard.this.startActivity(intent);
        }
        else if (v.equals(active)) {
            Intent intent = new Intent (this, ActiveUserListActivity.class);
            AdminDashboard.this.startActivity(intent);
        }
        else if (v.equals(banned)) {
            Intent intent = new Intent (this, BannedUserListActivity.class);
            AdminDashboard.this.startActivity(intent);
        }
        else if (v.equals(locked)) {
            Intent intent = new Intent (this, LockedUserListActivity.class);
            AdminDashboard.this.startActivity(intent);
        }
        else if (v.equals(profile)) {
            Intent intent = new Intent(this, ProfileActivity.class);
            AdminDashboard.this.startActivity(intent);

        }
    }
}
