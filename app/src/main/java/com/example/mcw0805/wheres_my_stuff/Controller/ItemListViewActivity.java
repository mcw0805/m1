package com.example.mcw0805.wheres_my_stuff.Controller;

import android.content.Intent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.mcw0805.wheres_my_stuff.Model.FoundItem;
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
public class ItemListViewActivity extends AppCompatActivity {

    /*
        ListView widget and its adapter
     */
    private ListView itemsLv;
    private ArrayAdapter<Item> itemAdapter;
    //private ItemAdapter itemAdapter;

    private EditText searchBarEdit;

    /*
        List of database reference keys of lost items
     */
    private List<String> itemKeys;

    /*
        List of LostItem objects, which are parcelable
     */
    private List<Item> itemObjectList;

    /*
        Map that contains the database snapshots.
        Key = variable names stored in the database
        Value = corresponding values for each of the variables
     */
    private Map<String, Object> itemMap;

    /*
        Database reference for the lost items in Firebase
     */
    private DatabaseReference itemsRef;

    private Spinner filterSpinner;

    private final String TAG = "ItemListActivity";

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_item_list);

        itemMap = new LinkedHashMap<>();
        itemKeys = new ArrayList<>();
        itemObjectList = new ArrayList<>();

        //spinner for filtering
        filterSpinner = (Spinner) findViewById(R.id.filter_spinner_lost);
        ArrayAdapter<ItemCategory> category_Adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, ItemCategory.values());
        category_Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        filterSpinner.setAdapter(category_Adapter);


        itemsLv = (ListView) findViewById(R.id.lost_list);
        //lostItemList = new ArrayList<>();

        searchBarEdit = (EditText) findViewById(R.id.searchBarEditLost);
        searchBarEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ItemListViewActivity.this.itemAdapter.getFilter().filter(s);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



        if (getIntent().getStringExtra("DashboardClikedListType").equals("LostItemListView")) {
            itemsRef = LostItem.getLostItemsRef();
        } else if (getIntent().getStringExtra("DashboardClikedListType").equals("FoundItemListView")) {
            itemsRef = FoundItem.getFoundItemsRef();
        }


        //References the list of lost items in Firebase
        itemsRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map<String, Object> item = (Map<String, Object>) dataSnapshot.getValue();

                //name of the item name
                String name = (String) item.get("name");

                Item polymorphicItem = null;
                if (getIntent().getStringExtra("DashboardClikedListType").equals("LostItemListView")) {

                    try {
                        polymorphicItem = LostItem.buildLostItemObject(dataSnapshot);
                    } catch (NullPointerException e) {
                        Log.d(TAG, "NullPointerException is caught.");
                        e.printStackTrace();
                    }

                    //adds the built object to the list
                    itemObjectList.add(polymorphicItem);

                    //puts the unique item key and all of its stored attributes
                    itemMap.put(dataSnapshot.getKey(), dataSnapshot.getValue());

                    //List of string that would be displayed on the screen
                    //lostItemList.add("(LOST) " + name);

                    itemKeys.add(dataSnapshot.getKey());

                    itemAdapter = new ArrayAdapter<>(getApplicationContext(),
                            R.layout.item_row_layout, R.id.textView, itemObjectList);
                    itemsLv.setAdapter(itemAdapter);
                    itemAdapter.notifyDataSetChanged();
                } else if (getIntent().getStringExtra("DashboardClikedListType").equals("FoundItemListView")) {
                    try {
                        polymorphicItem = FoundItem.buildFoundItemObject(dataSnapshot);
                    } catch (NullPointerException e) {
                        Log.d(TAG, "NullPointerException is caught.");
                        e.printStackTrace();
                    }

                    //adds the built object to the list
                    itemObjectList.add(polymorphicItem);

                    //puts the unique item key and all of its stored attributes
                    itemMap.put(dataSnapshot.getKey(), dataSnapshot.getValue());

                    //List of string that would be displayed on the screen
                    //lostItemList.add("(LOST) " + name);

                    itemKeys.add(dataSnapshot.getKey());

                    itemAdapter = new ArrayAdapter<>(getApplicationContext(),
                            R.layout.item_row_layout, R.id.textView, itemObjectList);
                    itemsLv.setAdapter(itemAdapter);
                    itemAdapter.notifyDataSetChanged();
                }
//                itemAdapter = new ItemAdapter(getApplicationContext(), itemObjectList);
//                itemsLv.setAdapter(itemAdapter);


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
        itemsLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                itemAdapter.notifyDataSetChanged();
                //itemAdapter.notifyDataSetChanged();

                Intent intent = new Intent(getApplicationContext(), ItemDescriptionActivity.class);
                if (getIntent().getStringExtra("DashboardClikedListType").equals("LostItemListView")) {
                    intent.putExtra("selectedLostItem", itemAdapter.getItem(position));
                } else if (getIntent().getStringExtra("DashboardClikedListType").equals("FoundItemListView")) {
                    intent.putExtra("selectedFoundItem", itemAdapter.getItem(position));
                }

                startActivity(intent);
            }
        });

        filterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                itemAdapter = new ArrayAdapter<>(getApplicationContext(),
                        R.layout.item_row_layout, R.id.textView,
                        filterByType((ItemCategory) filterSpinner.getSelectedItem()));
                itemsLv.setAdapter(itemAdapter);
                itemAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private List<Item> filterByType(ItemCategory cat) {

        if (cat == ItemCategory.NOTHING_SELECTED) {
            return itemObjectList;
        }

        List<Item> filteredItemList = new ArrayList<>();
        for (Item li : itemObjectList) {
            if (li.getCategory() == cat) {
                filteredItemList.add(li);
            }
        }

        return filteredItemList;

    }
}
