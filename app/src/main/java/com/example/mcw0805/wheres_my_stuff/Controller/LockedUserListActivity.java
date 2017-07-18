package com.example.mcw0805.wheres_my_stuff.Controller;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
 * @Author Ted Shang
 * @Version 1.0
 * Activity to see locked users and unlock necessary
 * Modeled off BannUserListActivity
 */
public class LockedUserListActivity extends AppCompatActivity {
    //Widget and adapter
    private android.widget.ListView lockedUserLv;
    private ArrayAdapter<User> lockedUserAdapter;
    //String representation
    private List<String> lockedUserList;
    //Key
    private List<String> lockedUserKeys;
    //User objects
    private List<User> lockedUserObjectList;
    /*  map to contain data snapshots
    Key == database keys / variable names
    Value == the values of each variable
    */
    private Map<String, Object> lockedMap;
    // database reference to all the users
    private DatabaseReference mUserRef;
    private final String tag = "LockedUserListActivity";

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locked_user_list);
        lockedMap = new LinkedHashMap<>();
        lockedUserKeys = new ArrayList<>();
        lockedUserObjectList = new ArrayList<>();
        lockedUserLv = (ListView) findViewById(R.id.locked_user_list);
        lockedUserList = new ArrayList<>();
        mUserRef = User.getUserRef();
        mUserRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map<String, Object> user = (Map<String, Object>) dataSnapshot.getValue();
                //name of user
                String name = (String) user.get("name");
                User newUser = User.buildUserObject(dataSnapshot);
                if (newUser.getIsLocked()) {
                    lockedUserObjectList.add(newUser);
                    lockedMap.put(dataSnapshot.getKey(), dataSnapshot.getValue());
                    //lockedUserList.add("(Locked) " + name);
                    lockedUserKeys.add(dataSnapshot.getKey());
                    lockedUserAdapter = new ArrayAdapter<User>(getApplicationContext(),
                            R.layout.item_row_layout, R.id.textView, lockedUserObjectList);
                    lockedUserLv.setAdapter(lockedUserAdapter);
                    lockedUserAdapter.notifyDataSetChanged();
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
        lockedUserLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                lockedUserAdapter.notifyDataSetChanged();
                Intent intent = new Intent(getApplicationContext(), UserDescriptionActivity.class);
                intent.putExtra("UserObj", lockedUserAdapter.getItem(position));
                startActivity(intent);
            }

        });
    }
}
