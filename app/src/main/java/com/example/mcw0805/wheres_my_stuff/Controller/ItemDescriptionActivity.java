package com.example.mcw0805.wheres_my_stuff.Controller;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.mcw0805.wheres_my_stuff.Model.FoundItem;
import com.example.mcw0805.wheres_my_stuff.Model.Item;
import com.example.mcw0805.wheres_my_stuff.Model.ItemType;
import com.example.mcw0805.wheres_my_stuff.Model.LostItem;
import com.example.mcw0805.wheres_my_stuff.R;

import java.io.IOException;
import java.text.DateFormat;
import java.util.List;
import java.util.Locale;

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
    private TextView status;

    private TextView rewardLabel;

    private Geocoder geocoder;

    private String itemOwnerUid;
    private Item selected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_description);

        Intent intent = getIntent();
        geocoder = new Geocoder(this, Locale.getDefault());

        itemOwnerUid = intent.getStringExtra("itemOwnerUid");

//        LostItem selectedLostItem = null;
//        FoundItem selectedFoundItem = null;
//        if (intent.getParcelableExtra("selectedLostItem") != null
//                && intent.getParcelableExtra("selectedLostItem") instanceof LostItem) {
//            selectedLostItem = intent.getParcelableExtra("selectedLostItem");
//        } else if (intent.getParcelableExtra("selectedFoundItem") != null
//                && intent.getParcelableExtra("selectedFoundItem") instanceof FoundItem) {
//            selectedFoundItem = intent.getParcelableExtra("selectedFoundItem");
//
//        }

        selected = intent.getParcelableExtra("selected");

        /*
        * sets all of the textViews that are specific to each object
         */
        name = (TextView) findViewById(R.id.item_name);
        description = (TextView) findViewById(R.id.item_description);
        status = (TextView) findViewById(R.id.item_curr_status);
        category = (TextView) findViewById(R.id.item_category);
        location = (TextView) findViewById(R.id.item_location);
        type = (TextView) findViewById(R.id.item_type);
        date = (TextView) findViewById(R.id.item_post_date);
        reward = (TextView) findViewById(R.id.item_reward);
        rewardLabel = (TextView) findViewById(R.id.item_rew);

        if (selected != null) {
            name.setText("" + selected.getName());
            description.setText("" + selected.getDescription());
            category.setText(selected.getCategory().toString());
            status.setText(selected.getStatusString());
            location.setText("temp");
            DateFormat df = new java.text.SimpleDateFormat("yyyy MMMM dd hh:mm aaa");
            date.setText(df.format(selected.getDate()));

            if (selected instanceof LostItem) {
                LostItem li = (LostItem) selected;
                type.setText(li.getItemType().toString());
                reward.setText("$" + li.getReward());
            } else if (selected instanceof FoundItem) {
                type.setText(((FoundItem) selected).getItemType().toString());
                reward.setVisibility(View.INVISIBLE);
                rewardLabel.setVisibility(View.INVISIBLE);
            }

            List<Address> addresses = null;
            try {
                addresses = geocoder.getFromLocation(selected.getLatitude(), selected.getLongitude(), 1);
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                location.setText(getLocationString(addresses.get(0)));
            } catch (IndexOutOfBoundsException e) {
                location.setText("Probably ain't on land");
            }

        }


//        if (selectedLostItem != null) {
//            name.setText("" + selectedLostItem.getName());
//            description.setText("" + selectedLostItem.getDescription());
//            category.setText(selectedLostItem.getCategory().toString());
//            location.setText("temp");
//            type.setText(selectedLostItem.getItemType().toString());
//            DateFormat df = new java.text.SimpleDateFormat("yyyy MMMM dd hh:mm aaa");
//            date.setText(df.format(selectedLostItem.getDate()));
//            reward.setText("$" + selectedLostItem.getReward());
//
//            if (selectedLostItem.getIsOpen()) {
//                status.setText("Open");
//            } else {
//                status.setText("Closed");
//            }
//
//            List<Address> addresses = null;
//            try {
//                addresses = geocoder.getFromLocation(selectedLostItem.getLatitude(), selectedLostItem.getLongitude(), 1);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            try {
//                location.setText(getLocationString(addresses.get(0)));
//            } catch (IndexOutOfBoundsException e) {
//                location.setText("Probably ain't on land");
//            }
//
//        } else if (selectedFoundItem != null) {
//            name.setText("" + selectedFoundItem.getName());
//            description.setText("" + selectedFoundItem.getDescription());
//            category.setText("" + selectedFoundItem.getCategory().toString());
//            type.setText(selectedFoundItem.getItemType().toString());
//            DateFormat df = new java.text.SimpleDateFormat("yyyy MMMM dd hh:mm aaa");
//            date.setText(df.format(selectedFoundItem.getDate()));
//            reward.setVisibility(View.INVISIBLE);
//            rewardLabel.setVisibility(View.INVISIBLE);
//
//            if (selectedFoundItem.getIsOpen()) {
//                status.setText("Open");
//            } else {
//                status.setText("Closed");
//            }
//
//            List<Address> addresses = null;
//            try {
//                addresses = geocoder.getFromLocation(selectedFoundItem.getLatitude(), selectedFoundItem.getLongitude(), 1);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            try {
//                location.setText(getLocationString(addresses.get(0)));
//            } catch (IndexOutOfBoundsException e) {
//                location.setText("Probably ain't on land");
//            }
//
//
//        }


    }

    private String getLocationString(Address address) {
        assert (address != null);
        String loc = "";
        if (address.getLocality() != null) {
            loc += address.getLocality();
        } else if (address.getAdminArea() != null) {
            loc += address.getAdminArea();
        }

        loc += "\nLatitude: " + address.getLatitude()
                + "\nLongitude: " + address.getLongitude();

        return loc;

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
