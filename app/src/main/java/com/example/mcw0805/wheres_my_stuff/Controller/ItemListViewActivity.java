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

import android.widget.Filter;
import android.widget.ListView;
import android.widget.Spinner;


import com.example.mcw0805.wheres_my_stuff.Model.FoundItem;
import com.example.mcw0805.wheres_my_stuff.Model.Item;
import com.example.mcw0805.wheres_my_stuff.Model.ItemCategory;
import com.example.mcw0805.wheres_my_stuff.Model.ItemType;
import com.example.mcw0805.wheres_my_stuff.Model.LostItem;
import com.example.mcw0805.wheres_my_stuff.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

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
         Widgets and adapters
     */
    private ListView itemsLv;
    //private ArrayAdapter<Item> itemAdapter;
    private ItemAdapter itemAdapter;
    private Spinner filterSpinner;
    private ItemType currentType;
    private EditText searchBarEdit;

    /*
        List of LostItem objects, which are parcelable
     */
    private List<Item> itemObjectList;
    private List<Item> copyList;

    private Map<Integer, String> itemUserMap;


    /*
        Database reference for the lost items in Firebase
     */
    private DatabaseReference itemsRef;

    private final String TAG = "ItemListActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        itemObjectList = new ArrayList<>();
        itemUserMap = new LinkedHashMap<>();

        //spinner for filtering
        filterSpinner = (Spinner) findViewById(R.id.filter_spinner_lost);
        final ArrayAdapter<ItemCategory> categoryAdapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item, ItemCategory.values());
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        filterSpinner.setAdapter(categoryAdapter);

        itemsLv = (ListView) findViewById(R.id.item_listView);

        searchBarEdit = (EditText) findViewById(R.id.searchBarEdit);


        /* filtering based on the text typed */
        searchBarEdit.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.i(TAG, "Before change: " + s.toString());

            }

            @Override
            public void onTextChanged(final CharSequence s, final int start, final int before, int count) {
                itemAdapter.getFilter().filter(s, new Filter.FilterListener() {
                    @Override
                    public void onFilterComplete(int count) {
                        Log.i(TAG, "Item Count: " + count);
                        Log.i(TAG, "Search bar current: " + searchBarEdit.getText().toString());

                    }
                });

                itemAdapter.notifyDataSetChanged();

            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.i(TAG, "After text change: " + s.toString());

            }

        });


        if (getIntent().getStringExtra("DashboardClickedListType").equals("LostItemListView")) {
            itemsRef = LostItem.getLostItemsRef();
            currentType = ItemType.LOST;
        } else if (getIntent().getStringExtra("DashboardClickedListType").equals("FoundItemListView")) {
            itemsRef = FoundItem.getFoundItemsRef();
            currentType = ItemType.FOUND;
        }

        if (itemsRef != null) {
            itemsRef.orderByChild("date");
        }


        itemsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Item.getObjectListFromDB(dataSnapshot, itemObjectList, currentType, itemUserMap);
                copyList = new ArrayList<>(itemObjectList);
                if (itemObjectList.size() > 0) {
//                    itemAdapter = new ArrayAdapter<>(getApplicationContext(),
//                            R.layout.item_row_layout, R.id.textView, itemObjectList);
                    itemAdapter = new ItemAdapter(getApplicationContext(),
                            R.layout.item_row_layout, itemObjectList);
                    itemsLv.setAdapter(itemAdapter);
                } else {
                    Log.w(TAG, "NO DATA");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        /* when some lost item in the ListView is clicked */
        itemsLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                itemAdapter.notifyDataSetChanged();

                Intent intent = new Intent(getApplicationContext(), ItemDescriptionActivity.class);
                intent.putExtra("itemOwnerUid", itemUserMap.get(position));

                if (getIntent().getStringExtra("DashboardClickedListType").equals("LostItemListView")) {
                    intent.putExtra("selectedLostItem", itemAdapter.getItem(position));
                } else if (getIntent().getStringExtra("DashboardClickedListType").equals("FoundItemListView")) {
                    intent.putExtra("selectedFoundItem", itemAdapter.getItem(position));
                }

                startActivity(intent);
            }
        });


        /* filters out the ListView based on the category selected */
        filterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                itemAdapter = new ArrayAdapter<>(getApplicationContext(),
//                        R.layout.item_row_layout, R.id.textView,
//                        filterByType((ItemCategory) filterSpinner.getSelectedItem()));

                itemAdapter = new ItemAdapter(getApplicationContext(), R.layout.item_row_layout,
                        filterByType((ItemCategory) filterSpinner.getSelectedItem()));

                itemsLv.setAdapter(itemAdapter);
                itemAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    /**
     * Filters the list of items based on the chosen category.
     *
     * @param cat category of the item
     * @return list containing the specified category
     */
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