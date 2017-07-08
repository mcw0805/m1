package com.example.mcw0805.wheres_my_stuff.Controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.mcw0805.wheres_my_stuff.Model.FoundItem;
import com.example.mcw0805.wheres_my_stuff.Model.LostItem;
import com.example.mcw0805.wheres_my_stuff.R;

import java.text.DateFormat;

/**
 * Class that controls the description of the lost items that the user
 * selected from the list
 *
 * @author Chianne Connelly, Chaewon Min
 */
public class ItemDescriptionActivity extends AppCompatActivity {

    /*
        TextViews for the various textfields in the lostItemDescription view
    */
    private TextView name;
    private TextView description;
    private TextView category;
    private TextView location;
    private TextView type;
    private TextView reward;
    private TextView date;

    private TextView rewardLabel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_description);

        Intent intent = getIntent();


        LostItem selectedLostItem = null;
        FoundItem selectedFoundItem = null;
        if (intent.getParcelableExtra("selectedLostItem") != null
                && intent.getParcelableExtra("selectedLostItem") instanceof LostItem) {
            selectedLostItem = intent.getParcelableExtra("selectedLostItem");
        } else if (intent.getParcelableExtra("selectedFoundItem") != null
                && intent.getParcelableExtra("selectedFoundItem") instanceof FoundItem) {
            selectedFoundItem = intent.getParcelableExtra("selectedFoundItem");

        }

        /*
        * sets all of the textViews that are specific to each object
         */
        name = (TextView) findViewById(R.id.item_name);
        description = (TextView) findViewById(R.id.item_description);
        category = (TextView) findViewById(R.id.item_category);
        location = (TextView) findViewById(R.id.item_location);
        type = (TextView) findViewById(R.id.item_type);
        date = (TextView) findViewById(R.id.item_post_date);
        reward = (TextView) findViewById(R.id.item_reward);
        rewardLabel = (TextView) findViewById(R.id.item_rew);



    if (selectedLostItem != null) {
        name.setText("" + selectedLostItem.getName());
        description.setText("" + selectedLostItem.getDescription());
        category.setText(selectedLostItem.getCategory().toString().toLowerCase());
        location.setText("temp");
        type.setText(selectedLostItem.getItemType().toString());
        DateFormat df = new java.text.SimpleDateFormat("yyyy MMMM dd hh:mm aaa");
        date.setText(df.format(selectedLostItem.getDate()));
        reward.setText("$" + selectedLostItem.getReward());
    } else if (selectedFoundItem != null) {
        name.setText("" + selectedFoundItem.getName());
        description.setText("" + selectedFoundItem.getDescription());
        category.setText("" + selectedFoundItem.getCategory().toString().toLowerCase());
        location.setText("temp");
        type.setText(selectedFoundItem.getItemType().toString());
        DateFormat df = new java.text.SimpleDateFormat("yyyy MMMM dd hh:mm aaa");
        date.setText(df.format(selectedFoundItem.getDate()));
        reward.setVisibility(View.INVISIBLE);
        rewardLabel.setVisibility(View.INVISIBLE);

    }


    }

//    private void setFields(Item item) {
//
//        if (item instanceof LostItem) {
//            selecetdItem = (LostItem) item;
//
//        } else if (item instanceof  FoundItem) {
//            selecetdItem = (FoundItem) item;
//        }
//
//    }
}
