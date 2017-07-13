package com.example.mcw0805.wheres_my_stuff.Controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.*;
import android.widget.ListView;

import com.example.mcw0805.wheres_my_stuff.Model.DonationItem;
import com.example.mcw0805.wheres_my_stuff.Model.Item2;
import com.example.mcw0805.wheres_my_stuff.Model.ItemCategory;
import com.example.mcw0805.wheres_my_stuff.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Class that controls the list of the found items that the users have
 * submitted in the Firebase
 *
 * @author Chianne Connelly, Chaewon Min
 * @versionas As of 7/8, it is outdated. This is disconnected from other classes.
 */

public class FoundItemListActivity extends AppCompatActivity {

    /*
        ListView widget and its adapter
     */
    private ListView donationItemLv;
    private ArrayAdapter<Item2> donationItemAdapter;
    //private ItemAdapter itemAdapter;

    private EditText searchBarEdit;

    /*
        Places list of text that would be shown in the ListView
     */
    //private List<String> foundItemList;

    /*
        List of database reference keys of found items
     */
    private List<String> foundItemKeys;

    /*
        List of FoundItem objects, which are parcelable
     */
    private List<Item2> donationObjectList;
    /*
        Map that contains the database snapshots.
        Key = variable names stored in the database
        Value = corresponding values for each of the variables
     */
    private Map<String, Object> foundMap;
    /*
        Database reference for the found items in Firebase
     */
    private DatabaseReference donateRef;
    private Spinner filterSpinner;


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
        donationObjectList = new ArrayList<>();

        donationItemLv = (ListView) findViewById(R.id.found_list);
        //foundItemList = new ArrayList<>();

        //References the list of lost items in Firebase
        donateRef = DonationItem.getDonationRef();
        filterSpinner = (Spinner) findViewById(R.id.filter_spinner_found);

        final ArrayAdapter<ItemCategory> category_Adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, ItemCategory.values());
        category_Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        filterSpinner.setAdapter(category_Adapter);

        searchBarEdit = (EditText) findViewById(R.id.searchBarEditFound);
        searchBarEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                FoundItemListActivity.this.donationItemAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        donateRef = DonationItem.getDonationRef();
        donateRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                getNeedItemUpdate(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        //when some found item in the list view is clicked
        donationItemLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                donationItemAdapter.notifyDataSetChanged();

                Intent intent = new Intent(getApplicationContext(), FoundItemDescription.class);

                intent.putExtra("selectedFoundItem", donationItemAdapter.getItem(position));


                startActivity(intent);
            }
        });
    }


    private void getNeedItemUpdate(DataSnapshot snapshot) {
        for (DataSnapshot ds : snapshot.getChildren()) {
            Item2 item = new DonationItem();
            item.setName(ds.getValue(Item2.class).getName());
            item.setDescription(ds.getValue(Item2.class).getDescription());
            item.setIsOpen(ds.getValue(Item2.class).getIsOpen());
            item.setCategory(ds.getValue(Item2.class).getCategory());
            item.setLatitude(ds.getValue(Item2.class).getLatitude());
            item.setLongitude(ds.getValue(Item2.class).getLongitude());
            item.setDate(ds.getValue(Item2.class).getDate());
            item.setUid(ds.getValue(Item2.class).getUid());

            donationObjectList.add(item);
        }

        if (donationObjectList.size() > 0) {
            donationItemAdapter = new ArrayAdapter<>(getApplicationContext(),
                    R.layout.item_row_layout, R.id.textView, donationObjectList);
            donationItemLv.setAdapter(donationItemAdapter);
        } else {
            Log.w(TAG, "NO DATA");
        }
    }
}
