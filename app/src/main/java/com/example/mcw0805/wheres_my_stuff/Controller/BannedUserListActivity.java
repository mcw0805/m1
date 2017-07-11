package com.example.mcw0805.wheres_my_stuff.Controller;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.example.mcw0805.wheres_my_stuff.R;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.example.mcw0805.wheres_my_stuff.Model.User;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author Ted Shang
 * @Verion 1.0
 * Activity to see banned users and unban them if necessary
 * Model off ActiveUserListActivity.java
 */
public class BannedUserListActivity extends AppCompatActivity {
    //Widget and adapter
    private android.widget.ListView bannedUserLv;
    private ArrayAdapter<User> bannedUserAdapter;
    //String representation
    private List<String> bannedUserList;
    //Key
    private List<String> bannedUserKeys;
    //User objects
    private List<User> bannedUserObjectList;
    /*  map to contain data snapshots
    Key == database keys / variable names
    Value == the values of each variable
    */
    private Map<String, Object> bannedMap;
    // database reference to all the users
    private DatabaseReference mUserRef;
    private final String TAG = "BannedUserListActivity";

    @Override
    protected void onStart() {super.onStart();}


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banned_user_list);
        bannedMap = new LinkedHashMap<>();
        bannedUserKeys = new ArrayList<>();
        bannedUserObjectList = new ArrayList<>();
        bannedUserLv = (ListView) findViewById(R.id.banned_user_list);
        bannedUserList = new ArrayList<>();
        mUserRef = User.getUserRef();
        mUserRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map<String,Object> user = (Map<String, Object>) dataSnapshot.getValue();
                // name of the user
                String name = (String) user.get("name");
                User newUser = User.buildUserObject(dataSnapshot);
                if (newUser.getIsBanned()) {
                    bannedUserObjectList.add(newUser);
                    bannedMap.put(dataSnapshot.getKey(), dataSnapshot.getValue());
                    bannedUserList.add("(Banned) " + name);
                    bannedUserKeys.add(dataSnapshot.getKey());
                    bannedUserAdapter = new ArrayAdapter<User>(getApplicationContext(),
                            R.layout.item_row_layout, R.id.textView, bannedUserObjectList);
                    bannedUserLv.setAdapter(bannedUserAdapter);
                    bannedUserAdapter.notifyDataSetChanged();
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
        bannedUserLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                bannedUserAdapter.notifyDataSetChanged();
                Log.d(TAG, "pos 1");
                Intent intent = new Intent(getApplicationContext(), UserDescriptionActivity.class);
                Log.d(TAG, "pos 2");
                intent.putExtra("UserObj", bannedUserAdapter.getItem(position));
                Log.d(TAG, "pos 3");
                startActivity(intent);
            }
        });
    }
}
