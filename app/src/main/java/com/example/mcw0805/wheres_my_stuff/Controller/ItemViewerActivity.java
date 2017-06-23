package com.example.mcw0805.wheres_my_stuff.Controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.mcw0805.wheres_my_stuff.Model.LostItem;
import com.example.mcw0805.wheres_my_stuff.R;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * THIS IS A TEMPORARY CLASS FOR SHOWING THE FULL DESCRIPTION THAT IS CLICKED.
 *
 * Need to make the LostItem object parcelable/serializable to prevent accessing the database twice
 */
public class ItemViewerActivity extends AppCompatActivity {

    private Map<String, Object> map;
    private int pos;
    private String itemKey;

    private TextView description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_viewer);

        description = (TextView) findViewById(R.id.test_desc);

        Intent intent = getIntent();
        map = (Map <String, Object>) intent.getSerializableExtra("map");
        pos = intent.getIntExtra("position", 0);
        itemKey = intent.getStringExtra("itemKey");

        Log.d("arraylist", map.values().toString());
        Log.d("value", map.get(itemKey).toString());
        Log.d("itemKey", itemKey);
        Log.d("passed map", map.toString());
        Log.d("passed int", ((Integer) pos).toString());








    }
}
