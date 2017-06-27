package com.example.mcw0805.wheres_my_stuff.Controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.mcw0805.wheres_my_stuff.Model.LostItem;
import com.example.mcw0805.wheres_my_stuff.R;

public class LostItemDescriptionActivity extends AppCompatActivity {

    private TextView name;
    private TextView description;
    private TextView category;
    private TextView location;
    private TextView type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_item_description);
        Intent intent = getIntent();
        //get lost item that was passed to this class from the LostItemListActivity
        LostItem k =intent.getParcelableExtra("selectedLostItem");
        name = (TextView) findViewById(R.id.item_name);
        name.setText("" + k.getName());
        description = (TextView) findViewById(R.id.item_description);
        description.setText("" + k.getDescription());
        category = (TextView) findViewById(R.id.item_category);
        category.setText("" + k.getCategory());
        location = (TextView) findViewById(R.id.item_location);
        location.setText("temp");
        type = (TextView) findViewById(R.id.item_type);
        type.setText("Lost");
    }
}
