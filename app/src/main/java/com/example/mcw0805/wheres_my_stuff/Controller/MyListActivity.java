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
import com.example.mcw0805.wheres_my_stuff.Model.ItemType;
import com.example.mcw0805.wheres_my_stuff.Model.LostItem;
import com.example.mcw0805.wheres_my_stuff.Model.User;
import com.example.mcw0805.wheres_my_stuff.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyListActivity extends AppCompatActivity {

    /*
    ListView widget and its adapter
 */
    private ListView myItemListView;
    private ArrayAdapter<Item> myItemAdapter;

    private Map<Integer, String> itemUserMap = new HashMap<>();
    /*
        List of LostItem objects, which are parcelable
     */
    private List<Item> myItemObjectList;


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

        myItemObjectList = new ArrayList<>();
        myItemListView = (ListView) findViewById(R.id.item_listView);


        mLostItemRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Item.getUserObjectList(dataSnapshot, myItemObjectList,
                        ItemType.LOST, uid, itemUserMap);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mFoundItemRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Item.getUserObjectList(dataSnapshot, myItemObjectList,
                        ItemType.FOUND, uid, itemUserMap);
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

                intent.putExtra("selected", myItemAdapter.getItem(position));
                intent.putExtra("itemOwnerUid", itemUserMap.get(position));
                startActivity(intent);

                Intent editIntent = getIntent();
                if (editIntent != null) {
                    finish();
                }
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
