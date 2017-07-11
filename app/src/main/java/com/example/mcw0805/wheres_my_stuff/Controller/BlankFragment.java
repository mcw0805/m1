package com.example.mcw0805.wheres_my_stuff.Controller;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.mcw0805.wheres_my_stuff.Model.FoundItem;
import com.example.mcw0805.wheres_my_stuff.Model.Item;
import com.example.mcw0805.wheres_my_stuff.Model.LostItem;
import com.example.mcw0805.wheres_my_stuff.Model.User;
import com.example.mcw0805.wheres_my_stuff.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class BlankFragment extends Fragment {



    private List<Item> itemObjectList;
    private Map<String, Object> itemMap;
    private DatabaseReference itemsRef = FoundItem.getFoundItemsRef();
    private ArrayAdapter<Item> itemAdapter;
    private final String TAG = "whatever";
    private ListView itemLv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView =  inflater.inflate(R.layout.fragment_blank, container, false);


        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currUser = mAuth.getCurrentUser();
        if (currUser!= null) {
            Log.d(TAG, currUser.getUid());
        }
        itemLv = (ListView) rootView.findViewById(R.id.found_list);
        itemMap = new LinkedHashMap<>();
        itemObjectList = new ArrayList<>();


        itemsRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map<String, Object> item = (Map<String, Object>) dataSnapshot.getValue();

                //name of the item name
                String name = (String) item.get("name");

                Item polymorphicItem = null;
                try {
                    polymorphicItem = FoundItem.buildFoundItemObject(dataSnapshot);
                } catch (NullPointerException e) {
                    Log.d(TAG, "NullPointerException is caught.");
                    e.printStackTrace();
                }

                //adds the built object to the list
                itemObjectList.add(polymorphicItem);

                itemMap.put(dataSnapshot.getKey(), dataSnapshot.getValue());


                itemAdapter = new ArrayAdapter<>(getActivity(),
                        R.layout.item_row_layout, R.id.textView, itemObjectList);
                itemLv.setAdapter(itemAdapter);
                itemAdapter.notifyDataSetChanged();

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

        return rootView;
    }



}
