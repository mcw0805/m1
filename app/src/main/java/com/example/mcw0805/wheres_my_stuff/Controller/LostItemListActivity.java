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

public class LostItemListActivity extends AppCompatActivity {

    private ListView lostItemLv;
    private ArrayAdapter<String> lostItemAdapter;
    private DatabaseReference mLostItemsRef;
    private DatabaseReference mItemRef;
    private List<String> lostItemList;
    private List<String> lostItemKeys;
    private List<LostItem> lostItemObjectList;
    private Map<String, Object> lostMap;
    private String itemKey;
    private int pos;


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

        mLostItemsRef = FirebaseDatabase.getInstance().getReference("posts/lost-items/");
        itemKey = mLostItemsRef.getKey();
        mItemRef = mLostItemsRef.child(itemKey);


        mLostItemsRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map<String, Object> item = (Map<String, Object>) dataSnapshot.getValue();

                //name of the item name
                String name = (String) item.get("name");

                //builds the LostItem object
                LostItem lostItem = LostItem.buildLostItem(dataSnapshot);

                //adds the built object to the list
                lostItemObjectList.add(lostItem);

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


        lostItemLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                pos = position;
                lostItemAdapter.notifyDataSetChanged();

                Intent intent = new Intent(getApplicationContext(), ItemViewerActivity.class);

                //passes the selected object to the next screen
                intent.putExtra("selectedLostItem", lostItemObjectList.get(position));

                startActivity(intent);
            }
        });

    }
}
