package com.example.mcw0805.wheres_my_stuff.Controller;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import android.widget.ListView;

import com.example.mcw0805.wheres_my_stuff.Model.FoundItem;
import com.example.mcw0805.wheres_my_stuff.Model.Item;
import com.example.mcw0805.wheres_my_stuff.Model.ItemCategory;
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
 * Class that controls the list of the found items that the users have
 * submitted in the Firebase
 *
 * @author Chianne Connelly, Chaewon Min
 * @versionas As of 7/8, it is outdated. This is disconnected from other classes.
 */

public class FoundItemListFragment extends Fragment {

    /*
        ListView widget and its adapter
     */
    private ListView foundItemLv;
    private ArrayAdapter<Item> foundItemAdapter;
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
    private List<Item> foundItemObjectList;
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
    private Spinner filterSpinner;


    private final String TAG = "FoundItemListFragment";

    @Override
    public void onStart() {
        super.onStart();
    }

    public View onCreate(LayoutInflater inflater, ViewGroup container,
                         Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_found_item_list,container, false);

        foundMap = new LinkedHashMap<>();
        foundItemKeys = new ArrayList<>();
        foundItemObjectList = new ArrayList<>();

        foundItemLv = (ListView) getView().findViewById(R.id.found_list);
        //foundItemList = new ArrayList<>();

        //References the list of lost items in Firebase
        mFoundItemsRef = FoundItem.getFoundItemsRef();
        filterSpinner = (Spinner) getView().findViewById(R.id.filter_spinner_found);

        final ArrayAdapter<ItemCategory> category_Adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, ItemCategory.values());
        category_Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        filterSpinner.setAdapter(category_Adapter);

        searchBarEdit = (EditText) getView().findViewById(R.id.searchBarEditFound);
        searchBarEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                FoundItemListFragment.this.foundItemAdapter.getFilter().filter(s);
                //FoundItemListActivity.this.itemAdapter.getFilter().filter(s);
                //FoundItemListActivity.this.copyAdapter.getFilter().filter(s);


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        mFoundItemsRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map<String, Object> item = (Map<String, Object>) dataSnapshot.getValue();

                //name of the item name
                //String name = (String) item.get("name");

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
                //foundItemList.add("(FOUND) " + name);

                foundItemKeys.add(dataSnapshot.getKey());

                foundItemAdapter = new ArrayAdapter<>(getActivity().getApplicationContext(),
                        R.layout.item_row_layout, R.id.textView, foundItemObjectList);
                foundItemLv.setAdapter(foundItemAdapter);
                foundItemAdapter.notifyDataSetChanged();


//                itemAdapter = new ItemAdapter(getApplicationContext(), foundItemObjectList);
//
//                foundItemLv.setAdapter(itemAdapter);
//                itemAdapter.notifyDataSetChanged();

//                copyAdapter = new ItemListAdapter(getApplicationContext(),
//                        R.layout.item_row_layout, R.id.textView, foundItemObjectList);
//                foundItemLv.setAdapter(copyAdapter);
//                copyAdapter.notifyDataSetChanged();
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

        //when some found item in the list view is clicked
        foundItemLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                foundItemAdapter.notifyDataSetChanged();
                //itemAdapter.notifyDataSetChanged();
                //copyAdapter.notifyDataSetChanged();

                Intent intent = new Intent(getActivity().getApplicationContext(), FoundItemDescription.class);

                //passes the selected object to the next screen
                intent.putExtra("selectedFoundItem", foundItemAdapter.getItem(position));
                //intent.putExtra("selectedFoundItem", itemAdapter.getItem(position));
                //intent.putExtra("selectedFoundItem", copyAdapter.getItem(position));

                startActivity(intent);
            }
        });
        return view;
    }
}
