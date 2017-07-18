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
import com.example.mcw0805.wheres_my_stuff.Model.LostItem;
import com.example.mcw0805.wheres_my_stuff.Model.User;
import com.example.mcw0805.wheres_my_stuff.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
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
    private TextView posterNickname;

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
        Log.d("OWNER", itemOwnerUid);

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
        posterNickname = (TextView) findViewById(R.id.item_owner_name);

        if (selected != null) {
            setPosterName(itemOwnerUid);

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

    }

    /**
     * Returns the string of the location
     * @param address the lat long coordinates
     * @return the location in string form
     */
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

    /**
     * sets the name with the poster's
     * @param uid uid of the user
     */
    private void setPosterName(String uid) {
        DatabaseReference userRef = User.getUserRef().child(uid).child("name");

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                posterNickname.setText(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

}
