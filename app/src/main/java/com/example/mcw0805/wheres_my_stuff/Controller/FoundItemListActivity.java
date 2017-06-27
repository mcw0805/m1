package com.example.mcw0805.wheres_my_stuff.Controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import android.widget.ListView;

import com.example.mcw0805.wheres_my_stuff.Model.FoundItem;
import com.example.mcw0805.wheres_my_stuff.Model.LostItem;
import com.example.mcw0805.wheres_my_stuff.R;
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
 * Class that controls the list of the found items that the users have
 * submitted in the Firebase
 *
 * @author Chianne Connelly
 */

public class FoundItemListActivity extends AppCompatActivity {

    /*
        ListView widget and its adapter
     */
    private android.widget.ListView foundItemLv;
    private ArrayAdapter<String> foundItemAdapter;

    /*
        Places list of text that would be shown in the ListView
     */
    private List<String> foundItemList;
    /*
        List of database reference keys of found items
     */
    private List<String> foundItemKeys;

    /*
        List of FoundItem objects, which are parcelable
     */
    private List<FoundItem> foundItemObjectList;
    /*
        Map that contains the database snapshots.
        Key = variable names stored in the database
        Value = corresponding values for each of the variables
     */
    private Map<String, Object> foundMap;
    /*
        Database reference for the found items in Firebase
     */
    private DatabaseReference mFoundItemsRef;
    private DatabaseReference mItemRef;

    private final String TAG = "FoundItemListActivity";

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_found_item_list);

        foundMap = new LinkedHashMap<>();
        foundItemKeys = new ArrayList<>();
        foundItemObjectList = new ArrayList<>();

        foundItemLv = (ListView) findViewById(R.id.found_list);
        foundItemList = new ArrayList<>();

        //References the list of lost items in Firebase
        mFoundItemsRef = FirebaseDatabase.getInstance().getReference("posts/found-items/");
        String itemKey = mFoundItemsRef.getKey(); //each item is associated with a key
        mItemRef = mFoundItemsRef.child(itemKey);

        mFoundItemsRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map<String, Object> item = (Map<String, Object>) dataSnapshot.getValue();

                //name of the item name
                String name = (String) item.get("name");

                FoundItem foundItem = null;
                try {
                    foundItem = FoundItem.buildFoundItemObject(dataSnapshot);
                } catch (NullPointerException e) {
                    Log.d(TAG, "NullPointerException is caught.");
                    e.printStackTrace();
                }
                //adds the built object to the list
                foundItemObjectList.add(foundItem);

                //puts the unique item key and all of its stored attributes
                foundMap.put(dataSnapshot.getKey(), dataSnapshot.getValue());

                //List of string that would be displayed on the screen
                foundItemList.add("(FOUND) " + name);

                foundItemKeys.add(dataSnapshot.getKey());

                foundItemAdapter = new ArrayAdapter<>(getApplicationContext(),
                        R.layout.activity_list_view, R.id.textView, foundItemList);
                foundItemLv.setAdapter(foundItemAdapter);
                foundItemAdapter.notifyDataSetChanged();
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

        /*
        //when some found item in the list view is clicked
        foundItemLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                foundItemAdapter.notifyDataSetChanged();

                Intent intent = new Intent(getApplicationContext(), FoundItemDescriptionActivity.class);

                //passes the selected object to the next screen
                intent.putExtra("selectedFoundItem", foundItemObjectList.get(position));

                startActivity(intent);
                finish();
            }
        });
        */
    }
}
