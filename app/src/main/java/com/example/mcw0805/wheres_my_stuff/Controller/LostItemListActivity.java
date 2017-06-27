package com.example.mcw0805.wheres_my_stuff.Controller;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.mcw0805.wheres_my_stuff.Model.Item;
import com.example.mcw0805.wheres_my_stuff.Model.LostItem;
import com.example.mcw0805.wheres_my_stuff.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Class that controls the list of the lost items that the users have
 * submitted in the Firebase
 *
 * @author Chaewon Min, Melanie Hall
 */
public class LostItemListActivity extends AppCompatActivity {

    /*
        ListView widget and its adapter
     */
    private ListView lostItemLv;
    private ArrayAdapter<String> lostItemAdapter;

    /*
        Places list of text that would be shown in the ListView
     */
    private List<String> lostItemList;

    /*
        List of databse reference keys of lost items
     */
    private List<String> lostItemKeys;

    /*
        List of LostItem objects, which are parcelable
     */
    private List<LostItem> lostItemObjectList;

    /*
        Map that contains the database snapshots.
        Key = variable names stored in the database
        Value = corresponding values for each of the variables
     */
    private Map<String, Object> lostMap;

    /*
        Database reference for the lost items in Firebase
     */
    private DatabaseReference mLostItemsRef;
    private DatabaseReference mItemRef;

    private final String TAG = "LostItemListActivity";

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_item_list);

        lostMap = new LinkedHashMap<>();
        lostItemKeys = new ArrayList<>();
        lostItemObjectList = new ArrayList<>();

//        ListView lost;
//        //Used for testing purposes.
//        String[] lostItems = {"Dog", "Cat", "Mouse", "Bird", "Elephant", "Doggo", "Giraffe", "Dolphin", "Tiger", "Lion", "Kitten"};
//
//        lost = (ListView) findViewById(R.id.lost_List);
//        ArrayAdapter<String> lost_List = new ArrayAdapter<>(this, R.layout.activity_list_view, R.id.textView, lostItems);
//        lost.setAdapter(lost_List);


        lostItemLv = (ListView) findViewById(R.id.lost_list);
        lostItemList = new ArrayList<>();

        //References the list of lost items in Firebase
        mLostItemsRef = FirebaseDatabase.getInstance().getReference("posts/lost-items/");

        String itemKey = mLostItemsRef.getKey(); //each item is associated with a key
        mItemRef = mLostItemsRef.child(itemKey);


        mLostItemsRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map<String, Object> item = (Map<String, Object>) dataSnapshot.getValue();

                //name of the item name
                String name = (String) item.get("name");

                LostItem lostItem = null;
                try {
                    lostItem = LostItem.buildLostItemObject(dataSnapshot);
                } catch (NullPointerException e) {
                    Log.d(TAG, "NullPointerException is caught.");
                    e.printStackTrace();
                }

                //adds the built object to the list
                lostItemObjectList.add(lostItem);

                //puts the unique item key and all of its stored attributes
                lostMap.put(dataSnapshot.getKey(), dataSnapshot.getValue());

                //List of string that would be displayed on the screen
                lostItemList.add("(LOST) " + name);

                lostItemKeys.add(dataSnapshot.getKey());

                lostItemAdapter = new ArrayAdapter<>(getApplicationContext(),
                        R.layout.activity_list_view, R.id.textView, lostItemList);
                lostItemLv.setAdapter(lostItemAdapter);
                lostItemAdapter.notifyDataSetChanged();

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


        //when some lost item in the list view is clicked
        lostItemLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                lostItemAdapter.notifyDataSetChanged();

                Intent intent = new Intent(getApplicationContext(), LostItemDescriptionActivity.class);

                //passes the selected object to the next screen
                intent.putExtra("selectedLostItem", lostItemObjectList.get(position));

                startActivity(intent);
                finish();
            }
        });

    }
}
