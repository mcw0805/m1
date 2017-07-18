package com.example.mcw0805.wheres_my_stuff.Controller;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;
import com.example.mcw0805.wheres_my_stuff.Model.FoundItem;
import com.example.mcw0805.wheres_my_stuff.Model.Item;
import com.example.mcw0805.wheres_my_stuff.Model.ItemCategory;
import com.example.mcw0805.wheres_my_stuff.Model.LostItem;
import com.example.mcw0805.wheres_my_stuff.Model.NeededItem;
import com.example.mcw0805.wheres_my_stuff.Model.User;
import com.example.mcw0805.wheres_my_stuff.R;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.util.ArrayList;
import java.util.List;
import com.example.mcw0805.wheres_my_stuff.Model.ItemType;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

/**
 * Controller for submitting the lost item.
 *
 * @author Melanie Hall, Chaewon Min, Ted Shang
 */
public class SubmitFormActivity extends AppCompatActivity
        implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    private final String tag = "SubmitFormActivity";

    /*
        Widgets for the item form page.
     */
    private EditText titleField;
    private EditText descriptField;
    private EditText rewardField;
    private TextView dollar;
    private TextView category;
    private Spinner categorySpinner;
    private Spinner typeSpinner;
    private Button backButton;
    private Button postButton;
    private EditText locationText;
    private TextView titleBlank;
    private TextView descriptionBlank;
    private TextView categoryBlank;
    private TextView typeBlank;
    private TextView addressBlank;


    /*
        Item object that would be created from the form.
     */
    private Item newItem;

    /*
        Firebase authorization, user
     */
    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;

    /*
        Field data that is retrived from the form.
     */
    private String inputName;
    private String inputDescription;
    private String uid;
    private double inputLatitude;
    private double inputLongitude;
    private ItemCategory inputItemCategory;
    private ItemType inputItemType;

    private List<User> users;
    private static final int PLACE_PICKER_REQUEST = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_submission_form);

        users = new ArrayList<>();

        /* instantiate widgets */
        titleField = (EditText) findViewById(R.id.title_L);
        descriptField = (EditText) findViewById(R.id.description_L);
        locationText = (EditText) findViewById(R.id.address); // address box
        rewardField = (EditText) findViewById(R.id.reward_L);
        dollar = (TextView) findViewById(R.id.dollar_L);
        category = (TextView) findViewById(R.id.category_L);
        categorySpinner = (Spinner) findViewById(R.id.category_Lspinner);
        typeSpinner = (Spinner) findViewById(R.id.type_Lspinner);
        postButton = (Button) findViewById(R.id.postButton_L);
        backButton = (Button) findViewById(R.id.backButton_L);
        titleBlank = (TextView) findViewById(R.id.titleBlank);
        categoryBlank = (TextView) findViewById(R.id.categoryBlank);
        addressBlank = (TextView) findViewById(R.id.addressBlank);
        descriptionBlank = (TextView) findViewById(R.id.descriptionBlank);
        typeBlank = (TextView) findViewById(R.id.typeBlank);


        ArrayAdapter<ItemCategory> categoryAdapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item, ItemCategory.values());
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoryAdapter);

        ArrayAdapter<ItemType> typeAdapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item, ItemType.values());
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(typeAdapter);

        /* reward field is visible only if LOST is selected from the typeSpinner */
        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).equals(ItemType.FOUND)) { //found
                    rewardField.setVisibility(View.INVISIBLE);
                    dollar.setVisibility(View.INVISIBLE);
                    categorySpinner.setVisibility(View.VISIBLE);
                    category.setVisibility(View.VISIBLE);
                } else if (parent.getItemAtPosition(position).equals(ItemType.LOST)) { //lost
                    rewardField.setVisibility(View.VISIBLE);
                    dollar.setVisibility(View.VISIBLE);
                    categorySpinner.setVisibility(View.VISIBLE);
                    category.setVisibility(View.VISIBLE);
                } else if (parent.getItemAtPosition(position).equals(ItemType.NEED) //need
                        || parent.getItemAtPosition(position).equals(ItemType.DONATION)) { //donation
                    rewardField.setVisibility(View.INVISIBLE);
                    dollar.setVisibility(View.INVISIBLE);
                    categorySpinner.setVisibility(View.INVISIBLE);
                    category.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }

        });

        titleField.clearFocus();

        /* set listener for the buttons */
        postButton.setOnClickListener(this);
        backButton.setOnClickListener(this);
        locationText.setOnClickListener(this);

        /* set Firebase authorization and get current user who is logged in */
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
        assert firebaseUser != null;

    }

    /**
     * onClick method that notifies user of submission/if
     * they wish to cancel submission.
     *
     * @param v checks if user submits/cancels submission
     */
    @Override
    public void onClick(View v) {
        if (v == postButton) {

            //initializes the fields in the form as private instance variables
            if (!setFieldVars()) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(SubmitFormActivity.this);
                builder1.setMessage("Please fill out all required fields");
                builder1.setCancelable(true);
                builder1.setPositiveButton(
                        "Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder1.create();
                alert.show();
                return;
            }

            //get current time
            long currentTime = System.currentTimeMillis();

            if (inputItemType == ItemType.LOST) {
                submitLostItem(currentTime).addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        Toast.makeText(SubmitFormActivity.this, "Post Added!", Toast.LENGTH_LONG).show();
                        Intent submitPostIntent = new Intent(SubmitFormActivity.this, Dashboard.class);
                        startActivity(submitPostIntent);
                    }
                });
            } else if (inputItemType == ItemType.FOUND) {
                submitFoundItem(currentTime).addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        Toast.makeText(SubmitFormActivity.this, "Post Added!", Toast.LENGTH_LONG).show();
                        Intent submitPostIntent = new Intent(SubmitFormActivity.this, Dashboard.class);
                        startActivity(submitPostIntent);
                    }
                });
            } else if (inputItemType == ItemType.NEED) {
                submitNeedItem(currentTime).addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        Toast.makeText(SubmitFormActivity.this, "Post Added!", Toast.LENGTH_LONG).show();
                        Intent submitPostIntent = new Intent(SubmitFormActivity.this, Dashboard.class);
                        startActivity(submitPostIntent);

                    }
                });
            } else if (inputItemType == ItemType.DONATION) {
                submitDonatedItem(currentTime).addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        Toast.makeText(SubmitFormActivity.this, "Post Added!", Toast.LENGTH_LONG).show();
                        Intent submitPostIntent = new Intent(SubmitFormActivity.this, Dashboard.class);
                        startActivity(submitPostIntent);

                    }
                });
            }

        }

        if (v == backButton) {
            startActivity(new Intent(this, Dashboard.class));
        }

        if (v == locationText) {
            startPlacePicker();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    /**
     * Method that submits lost item information to the database.
     * @param dateTime current date-time
     * @return task
     */
    private Task submitLostItem(long dateTime) {

        int reward = Integer.parseInt(rewardField.getText().toString());
        String pushKey = LostItem.getLostItemsRef().push().getKey();
        newItem = new LostItem(inputName, inputDescription, dateTime,
                inputLongitude, inputLatitude, inputItemCategory,
                uid, reward);
        incrementSubmissionCount();
        DatabaseReference child = LostItem.getLostItemsRef().child(uid + "--" + pushKey);

        return newItem.writeToDatabase(child);
    }

    /**
     * Method that submits found item information to the database.
     * @param dateTime current date-time
     * @return task
     */
    private Task submitFoundItem(long dateTime) {
        String pushKey = FoundItem.getFoundItemsRef().push().getKey();
        newItem = new FoundItem(inputName, inputDescription, dateTime,
                inputLongitude, inputLatitude, inputItemCategory, uid);
        incrementSubmissionCount();


        DatabaseReference child = FoundItem.getFoundItemsRef().child(uid + "--" + pushKey);
        return newItem.writeToDatabase(child);

    }
    /**
     * Method that submits need item information to the database.
     * @param dateTime current date-time
     * @return task
     */
    private Task submitNeedItem(long dateTime) {
        String pushKey = NeededItem.getNeededItemsRef().push().getKey();
        newItem = new NeededItem(inputName, inputDescription, dateTime,
                inputLongitude, inputLatitude, ItemCategory.MISC, pushKey);
        incrementSubmissionCount();
        DatabaseReference child = NeededItem.getNeededItemsRef().child(uid + "--" + pushKey);
        return newItem.writeToDatabase(child);

    }
    /**
     * Method that submits need donated information to the database.
     * @param dateTime current date-time
     * @return task
     */
    private Task submitDonatedItem(long dateTime) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference donationItemsRef = database.getReference("posts/donation-items/");
        String pushKey = donationItemsRef.push().getKey();
        newItem = new Item(inputName, inputDescription, dateTime,
                inputLongitude, inputLatitude, ItemCategory.MISC, uid);
        incrementSubmissionCount();
        DatabaseReference child = donationItemsRef.child(uid + "--" + pushKey);
        return newItem.writeToDatabase(child);
    }


    /**
     * Initializes the various texts/numbers from the form as instance data
     * to be used to create Item objects.
     * @return boolean whether or not the fields were validated
     */
    private boolean setFieldVars() {

        boolean valid = true;

        inputName = titleField.getText().toString();
        if (inputName.length() == 0) {
            titleBlank.setVisibility(View.VISIBLE);
        } else {
            titleBlank.setVisibility(View.INVISIBLE);
        }
        inputDescription = descriptField.getText().toString();
        if (inputDescription.length() == 0) {
            descriptionBlank.setVisibility(View.VISIBLE);
        } else {
            descriptionBlank.setVisibility(View.INVISIBLE);
        }
        inputItemType = (ItemType) typeSpinner.getSelectedItem();
        inputItemCategory = (ItemCategory) categorySpinner.getSelectedItem();

        uid = firebaseUser.getUid();

        if (rewardField.getVisibility() == View.VISIBLE && !FormValidation.isValidInteger(rewardField)) {
            valid = false;

            if (!valid) {
                Log.w(tag, "Reward must be an integer and cannot be empty.");
            }

        }
        if (inputLongitude == 0 && inputLatitude == 0) {
            addressBlank.setVisibility(View.VISIBLE);
        } else {
            addressBlank.setVisibility(View.INVISIBLE);
        }

        return valid;
    }

    /**
     * Gets the current user from the database and increments the count
     * of number of items posted.
     */
    private void incrementSubmissionCount() {
        final DatabaseReference itemCountRef = FirebaseDatabase.getInstance()
                .getReference("users/" + this.uid).child("itemCount");

        itemCountRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String key = dataSnapshot.getKey();
                String val = dataSnapshot.getValue().toString();

                if (key.equals("itemCount")) {
                    Integer count = Integer.parseInt(String.valueOf(val));
                    Log.i(tag, "Before incrementing- " + key + ": " + count);

                    count++;
                    Log.i(tag, "After incrementing- " + key + ": " + count);

                    itemCountRef.setValue(count);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    /**
     * begins the placepicker task
     */
    private void startPlacePicker() {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

        try {
            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);

        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    /**
     * Fills in the address after the place selected
     * @param requestCode request
     * @param resultCode result
     * @param data data
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(getApplicationContext(), data);
                String toastMsg = String.format("Place: %s", place.getName());
                Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
                locationText.setText(place.getAddress());
                LatLng tempLoc = place.getLatLng();
                inputLatitude = tempLoc.latitude;
                inputLongitude = tempLoc.longitude;
            }
        }
    }


}
