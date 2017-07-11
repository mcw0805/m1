package com.example.mcw0805.wheres_my_stuff.Controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.mcw0805.wheres_my_stuff.Model.User;
import com.example.mcw0805.wheres_my_stuff.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Class to controll the list of Active users for Admins
 */
public class ActiveUserListActivity extends AppCompatActivity {
    // widget and adapter
    private android.widget.ListView activeUserLv;
    private ArrayAdapter<User> activeUserAdapter;

    // List of text to show in the list view
    private List<String> activeUserList;

    // keys for all of our active users
    private List<String> activeUserKeys;

    // list of active users
    private List<User> activeUserObjectList;

    /*  map to contain data snapshots
        Key == database keys / variable names
        Value == the values of each variable
     */
    private Map<String, Object> userMap;

    // database reference to all the users
    private DatabaseReference mUserRef;
    private final String TAG = "ActiveUerListActivity";

    @Override
    protected void onStart() {super.onStart();}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_user_list);
        userMap = new LinkedHashMap<>();
        activeUserKeys = new ArrayList<>();
        activeUserObjectList = new ArrayList<>();

        // associating the var with the listview in the active users activity
        activeUserLv = (ListView) findViewById(R.id.active_user_list);
        activeUserList = new ArrayList<>();

        // gets reference to the database containing the users
        mUserRef = User.getUserRef();
        mUserRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map<String, Object> user = (Map<String, Object>) dataSnapshot.getValue();

                // name of the user
                String name = (String) user.get("name");

                User newUser = null;
                try {
                    newUser = User.buildUserObject(dataSnapshot);
                } catch (NullPointerException e) {
                    Log.d(TAG, "NullPointerException is caught.");
                    e.printStackTrace();
                }
                //adds the built object to the list
                if (!(newUser.getIsBanned()) && !(newUser.getIsLocked())) {
                    activeUserObjectList.add(newUser);
                    //puts the unique item key and all of its stored attributes
                    userMap.put(dataSnapshot.getKey(), dataSnapshot.getValue());

                    //List of string that would be displayed on the screen
                    //activeUserList.add("(Active) " + name);

                    activeUserKeys.add(dataSnapshot.getKey());

                    activeUserAdapter = new ArrayAdapter<User>(getApplicationContext(),
                            R.layout.item_row_layout, R.id.textView, activeUserObjectList);
                    activeUserLv.setAdapter(activeUserAdapter);
                    activeUserAdapter.notifyDataSetChanged();
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
        activeUserLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                activeUserAdapter.notifyDataSetChanged();
                Intent intent = new Intent(getApplicationContext(), UserDescriptionActivity.class);
                intent.putExtra("UserObj", activeUserAdapter.getItem(position));
                startActivity(intent);
            }
        });
    }
}
