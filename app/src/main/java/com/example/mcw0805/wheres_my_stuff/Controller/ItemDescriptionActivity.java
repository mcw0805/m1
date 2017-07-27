package com.example.mcw0805.wheres_my_stuff.Controller;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mcw0805.wheres_my_stuff.Model.FoundItem;
import com.example.mcw0805.wheres_my_stuff.Model.Item;
import com.example.mcw0805.wheres_my_stuff.Model.LostItem;
import com.example.mcw0805.wheres_my_stuff.Model.NeededItem;
import com.example.mcw0805.wheres_my_stuff.Model.User;
import com.example.mcw0805.wheres_my_stuff.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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
    private TextView posterEmail;

    private TextView rewardLabel;
    private TextView catLabel;
    private TextView imgLabel;

    private ImageView itemImg;

    private Geocoder geocoder;

    private String itemOwnerUid;
    private String itemKey;
    private Item selected;

    private final DatabaseReference imgRef = FirebaseDatabase.getInstance().getReference("pics");
    private final String TAG = "ItemDescriptionActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_description);


        Intent intent = getIntent();
        geocoder = new Geocoder(this, Locale.getDefault());

        itemOwnerUid = intent.getStringExtra("itemOwnerUid");
        itemKey = intent.getStringExtra("itemKey");
        Log.d("OWNER", itemOwnerUid);

        selected = intent.getParcelableExtra("selected");

        loadImage(itemKey);
        /*
             sets all of the textViews that are specific to each object
         */
        name = (TextView) findViewById(R.id.item_name);
        description = (TextView) findViewById(R.id.item_description);
        status = (TextView) findViewById(R.id.item_curr_status);
        category = (TextView) findViewById(R.id.item_category);
        catLabel = (TextView) findViewById(R.id.item_cat);
        location = (TextView) findViewById(R.id.item_location);
        type = (TextView) findViewById(R.id.item_type);
        date = (TextView) findViewById(R.id.item_post_date);
        reward = (TextView) findViewById(R.id.item_reward);
        rewardLabel = (TextView) findViewById(R.id.item_rew);
        posterEmail = (TextView) findViewById(R.id.item_owner_name);
        imgLabel = (TextView) findViewById(R.id.item_img_label);
        itemImg = (ImageView) findViewById(R.id.item_image_optional);


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
            } else if (selected instanceof NeededItem) {
                type.setText(((NeededItem) selected).getItemType().toString());
                reward.setVisibility(View.INVISIBLE);
                rewardLabel.setVisibility(View.INVISIBLE);
                category.setVisibility(View.GONE);
                catLabel.setVisibility(View.GONE);
            } else {
                type.setText(selected.getType().toString());
                reward.setVisibility(View.INVISIBLE);
                rewardLabel.setVisibility(View.INVISIBLE);
                category.setVisibility(View.GONE);
                catLabel.setVisibility(View.GONE);
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
            } catch (NullPointerException e) {
                location.setText("Loc");
            }


        }

        posterEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] receiverEmail = {posterEmail.getText().toString()};
                final String msgSubj = "Where's My Stuff- Regarding Item: " + name.getText().toString();


                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setData(Uri.parse("mailto:"));
                emailIntent.putExtra(Intent.EXTRA_EMAIL, receiverEmail);
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, msgSubj);

                emailIntent.setType("message/rfc822");

                try {
                    startActivity(Intent.createChooser(emailIntent, "Choose an email client..."));
                    //finish();
                    Log.i(TAG, "Email sending in progress...");
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(ItemDescriptionActivity.this,
                            "There is no email client installed.", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    /**
     * Returns the string of the location
     *
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
     *
     * @param uid uid of the user
     */
    private void setPosterName(String uid) {
        DatabaseReference userRef = User.getUserRef().child(uid).child("email");

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                posterEmail.setText((String) dataSnapshot.getValue());
                posterEmail.setPaintFlags(name.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    /**
     * Loads the image of the item from Firebase.
     *
     * @param itemKey item push key in the database
     */
    private void loadImage(String itemKey) {
        imgRef.child(itemKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    Bitmap bm = convertStringtoBitmap(dataSnapshot.getValue().toString());
                    itemImg.setImageBitmap(bm);
                    imgLabel.setVisibility(View.VISIBLE);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                    Log.i(TAG, "No Image to upload");
                    imgLabel.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    /**
     * Given an encoded string of an image, it converts it to a Bitmap object.
     *
     * @param encodedStr encoded string of an image
     * @return Bitmap form of the image
     */
    private Bitmap convertStringtoBitmap(String encodedStr) {
        try {
            byte[] encodeByte = Base64.decode(encodedStr, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }


}
