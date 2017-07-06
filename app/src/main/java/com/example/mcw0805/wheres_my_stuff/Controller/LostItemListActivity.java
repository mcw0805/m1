package com.example.mcw0805.wheres_my_stuff.Controller;

import android.content.Intent;
<<<<<<< HEAD
=======
import android.graphics.Color;
import android.os.Parcelable;
import android.support.transition.Scene;
>>>>>>> Trnsitions
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
<<<<<<< HEAD
=======
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
>>>>>>> Trnsitions
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.mcw0805.wheres_my_stuff.Model.Item;
import com.example.mcw0805.wheres_my_stuff.Model.ItemCategory;
import com.example.mcw0805.wheres_my_stuff.Model.LostItem;
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
    private ArrayAdapter<Item> lostItemAdapter;
    private ItemAdapter itemAdapter;

    private EditText searchBarEdit;
    /*
        Places list of text that would be shown in the ListView
     */
    //private List<String> lostItemList;

    /*
        List of database reference keys of lost items
     */
    private List<String> lostItemKeys;

    /*
        List of LostItem objects, which are parcelable
     */
    private List<Item> lostItemObjectList;

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
    private Spinner filterSpinner;

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

        //spinner for filtering
        filterSpinner = (Spinner) findViewById(R.id.filter_spinner_lost);
        ArrayAdapter<ItemCategory> category_Adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, ItemCategory.values());
        category_Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        filterSpinner.setAdapter(category_Adapter);


        lostItemLv = (ListView) findViewById(R.id.lost_list);
        //lostItemList = new ArrayList<>();

        searchBarEdit = (EditText) findViewById(R.id.searchBarEditLost);
        searchBarEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                LostItemListActivity.this.lostItemAdapter.getFilter().filter(s);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        //References the list of lost items in Firebase
        mLostItemsRef = LostItem.getLostItemsRef();

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
                //lostItemList.add("(LOST) " + name);

                lostItemKeys.add(dataSnapshot.getKey());

                lostItemAdapter = new ArrayAdapter<>(getApplicationContext(),
                        R.layout.item_row_layout, R.id.textView, lostItemObjectList);
                lostItemLv.setAdapter(lostItemAdapter);
                lostItemAdapter.notifyDataSetChanged();

//                itemAdapter = new ItemAdapter(getApplicationContext(), lostItemObjectList);
//                lostItemLv.setAdapter(itemAdapter);


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
                //itemAdapter.notifyDataSetChanged();

                Intent intent = new Intent(getApplicationContext(), LostItemDescriptionActivity.class);

                //passes the selected object to the next screen
                intent.putExtra("selectedLostItem", lostItemAdapter.getItem(position));

                startActivity(intent);
            }
        });
<<<<<<< HEAD



=======
>>>>>>> Trnsitions
    }
}
