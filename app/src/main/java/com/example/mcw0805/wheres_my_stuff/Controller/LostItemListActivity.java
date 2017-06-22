package com.example.mcw0805.wheres_my_stuff.Controller;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.ArrayAdapter;

import com.example.mcw0805.wheres_my_stuff.R;

public class LostItemListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_item_list);

        ListView lost;
        //Used for testing purposes.
        String[] lostItems = {"Dog", "Cat", "Mouse", "Bird", "Elephant", "Doggo", "Giraffe", "Dolphin", "Tiger", "Lion", "Kitten"};

        lost = (ListView) findViewById(R.id.lost_List);
        ArrayAdapter<String> lost_List = new ArrayAdapter<>(this, R.layout.activity_list_view, R.id.textView, lostItems);
        lost.setAdapter(lost_List);
    }
}
