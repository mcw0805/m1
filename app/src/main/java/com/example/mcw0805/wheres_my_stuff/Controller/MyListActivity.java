package com.example.mcw0805.wheres_my_stuff.Controller;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.mcw0805.wheres_my_stuff.Model.FoundItem;
import com.example.mcw0805.wheres_my_stuff.Model.Item;
import com.example.mcw0805.wheres_my_stuff.Model.LostItem;
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
    private ArrayAdapter<String> myItemAdapter;

    /*
        Places list of text that would be shown in the ListView
     */
    private List<String> myItemList;

    /*
        List of database reference keys of lost items
     */
    private List<String> lostItemKeys;

    /*
        List of LostItem objects, which are parcelable
     */
    private List<Item> itemObjectList;

    /*
        Map that contains the database snapshots.
        Key = variable names stored in the database
        Value = corresponding values for each of the variables
     */
    private Map<String, Object> lostMap;

    /*
        Database reference for the lost items in Firebase
     */
    private DatabaseReference mFoundItemRef;

    private FirebaseAuth mAuth;

    private final String TAG = "MyListItemActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_list);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        final String uid = user.getUid();

        myItemList = new ArrayList<>();
        itemObjectList = new ArrayList<>();
        myItemListView = (ListView) findViewById(R.id.my_list);
        mFoundItemRef = FoundItem.getFoundItemsRef();

        mFoundItemRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map<String, Object> item = (Map<String, Object>) dataSnapshot.getValue();

                //name of the item name
                String name = (String) item.get("name");


                String pushKey = dataSnapshot.getKey().toString();
                String[] parts = pushKey.split("---");
                String part1 = parts[0]; // 004

                Log.d("MYLIST", dataSnapshot.getKey().toString());
                Log.d("MYLISTI", part1);

                if (uid.equals(part1)) {
                    FoundItem foundItem = FoundItem.buildFoundItemObject(dataSnapshot);


                    itemObjectList.add(foundItem);
                    myItemList.add("MY ITEM " + name);
                }

                myItemAdapter = new ArrayAdapter<String>(getApplicationContext(),
                        R.layout.activity_list_view, R.id.textView, myItemList);

                myItemListView.setAdapter(myItemAdapter);
                myItemAdapter.notifyDataSetChanged();
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
    }
}
