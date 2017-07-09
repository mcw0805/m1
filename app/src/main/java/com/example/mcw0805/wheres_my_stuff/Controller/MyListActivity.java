package com.example.mcw0805.wheres_my_stuff.Controller;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.mcw0805.wheres_my_stuff.Model.FoundItem;
import com.example.mcw0805.wheres_my_stuff.Model.Item;
import com.example.mcw0805.wheres_my_stuff.Model.LostItem;
import com.example.mcw0805.wheres_my_stuff.Model.User;
import com.example.mcw0805.wheres_my_stuff.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MyListActivity extends AppCompatActivity {

    /*
    ListView widget and its adapter
 */
    private ListView myItemListView;
    private ArrayAdapter<Item> myItemAdapter;

    /*
        Places list of text that would be shown in the ListView
     */
    private List<String> myItemList;

    /*
        List of database reference keys of lost items
     */
    private ArrayList<String> myItemLostKeys;
    private ArrayList<String> myItemFoundKeys;
    private ArrayList<String> myItemKeys;

    /*
        List of LostItem objects, which are parcelable
     */
    private List<Item> myItemObjectList;

    /*
        Map that contains the database snapshots.
        Key = variable names stored in the database
        Value = corresponding values for each of the variables
     */
    private Map<String, Object> lostMap;

    /*
        Database reference for the lost items in Firebase
     */
    private final DatabaseReference mFoundItemRef = FoundItem.getFoundItemsRef();
    private final DatabaseReference mLostItemRef = LostItem.getLostItemsRef();

    /*
    Firebase authorization
 */
    private FirebaseAuth mAuth;
    private FirebaseUser currUser;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private boolean isAuthListenerSet = false;
    private DatabaseReference userRef = User.getUserRef();
    private String currUserUID;

    private final String TAG = "MyListItemActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

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

        currUser = mAuth.getCurrentUser();
        currUserUID = currUser.getUid();

        final String uid = currUser.getUid();

        myItemList = new ArrayList<>();
        myItemObjectList = new ArrayList<>();
        myItemListView = (ListView) findViewById(R.id.item_listView);

        myItemLostKeys = new ArrayList<>();
        myItemFoundKeys = new ArrayList<>();
        myItemKeys = new ArrayList<>();

        mLostItemRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map<String, Object> item = (Map<String, Object>) dataSnapshot.getValue();



                String pushKey = dataSnapshot.getKey().toString();
                String[] parts = pushKey.split("---");
                String itemUser = parts[0]; // 004

                if (itemUser.equals(uid)) {
                    LostItem lostItem = LostItem.buildLostItemObject(dataSnapshot);
                    myItemObjectList.add(lostItem);
                    myItemLostKeys.add(pushKey);
                    myItemKeys.add(pushKey);
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

        mFoundItemRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map<String, Object> item = (Map<String, Object>) dataSnapshot.getValue();



                String pushKey = dataSnapshot.getKey().toString();
                String[] parts = pushKey.split("---");
                String itemUser = parts[0]; // 004

                if (itemUser.equals(uid)) {
                    FoundItem foundItem = FoundItem.buildFoundItemObject(dataSnapshot);
                    myItemObjectList.add(foundItem);
                    myItemFoundKeys.add(pushKey);
                    myItemKeys.add(pushKey);
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

        myItemAdapter = new ArrayAdapter<>(getApplicationContext(),
                R.layout.item_row_layout, R.id.textView, myItemObjectList);
        myItemListView.setAdapter(myItemAdapter);
        myItemAdapter.notifyDataSetChanged();

        myItemListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), MyEditableItemActivity.class);
                intent.putExtra("selectedObjKey", myItemKeys.get(position));
                if (myItemAdapter.getItem(position) instanceof LostItem) {
                    intent.putExtra("selectedLostItem", myItemAdapter.getItem(position));
//                    intent.putExtra("selectedLostKey", myItemLostKeys.get(position));
                } else if (myItemAdapter.getItem(position) instanceof FoundItem) {
                    intent.putExtra("selectedFoundItem", myItemAdapter.getItem(position));
//                    intent.putExtra("selectedFoundKey", myItemFoundKeys.get(position));
                }

                startActivity(intent);
            }
        });



    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!isAuthListenerSet) {
            mAuth.addAuthStateListener(mAuthListener);
            isAuthListenerSet = true;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
            isAuthListenerSet = false;
        }
    }
}
