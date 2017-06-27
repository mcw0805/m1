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
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * THIS IS A TEMPORARY CLASS FOR SHOWING THE FULL DESCRIPTION THAT IS CLICKED.
 *
 * Need to make the LostItem object parcelable/serializable to prevent accessing the database twice
 */
public class ItemViewerActivity extends AppCompatActivity {

    private int pos;
    private String itemKey;

    private TextView description;
    private LostItem li;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_viewer);

        description = (TextView) findViewById(R.id.test_desc);

        Intent intent = getIntent();

        li = intent.getParcelableExtra("selectedLostItem");

        pos = intent.getIntExtra("position", 0);
        itemKey = intent.getStringExtra("itemKey");
        Boolean x = li == null;
        Log.d("lostobj", "bool -> want false " + x.toString());

        description.setText(li.getDescription());


    }
}
