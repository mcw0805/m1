package com.example.mcw0805.wheres_my_stuff.Controller;

import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.mcw0805.wheres_my_stuff.Model.FoundItem;
import com.example.mcw0805.wheres_my_stuff.Model.ItemType;
import com.example.mcw0805.wheres_my_stuff.Model.LostItem;
import com.example.mcw0805.wheres_my_stuff.R;

import java.text.DateFormat;

/**
 * Class that controls the description of the found items that the user
 * selected from the list
 *
 * @author Chianne Connelly
 * @versionAs of 7/8, it is outdated. This is disconnected from other classes.
 */
public class FoundItemDescription extends AppCompatActivity {
    
    /*
        TextViews for the various textfields in the foundItemDescription view
    */
    private TextView name;
    private TextView description;
    private TextView category;
    private TextView location;
    private TextView type;
    private TextView date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_found_item_description);

        Intent intent = getIntent();
        //get found item that was passed to this class from the FoundItemListActivity
        FoundItem selected =intent.getParcelableExtra("selectedFoundItem");
        /*
        * sets all of the textViews that are specific to each object
         */
        name = (TextView) findViewById(R.id.item_name);
        name.setText("" + selected.getName());
        description = (TextView) findViewById(R.id.item_description);
        description.setText("" + selected.getDescription());
        category = (TextView) findViewById(R.id.item_category);
        category.setText("" + selected.getCategory());
        location = (TextView) findViewById(R.id.item_location);
        location.setText("temp");
        type = (TextView) findViewById(R.id.item_type);
        type.setText(ItemType.FOUND.toString());
        date = (TextView) findViewById(R.id.found_post_date);
        DateFormat df = new java.text.SimpleDateFormat("yyyy MMMM dd hh:mm aaa");
        date.setText(df.format(selected.getDate()));
    }
}
