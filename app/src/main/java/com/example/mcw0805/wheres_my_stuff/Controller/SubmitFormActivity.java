package com.example.mcw0805.wheres_my_stuff.Controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mcw0805.wheres_my_stuff.Model.FoundItem;
import com.example.mcw0805.wheres_my_stuff.Model.Item;
import com.example.mcw0805.wheres_my_stuff.Model.ItemCategory;
import com.example.mcw0805.wheres_my_stuff.Model.LostItem;
import com.example.mcw0805.wheres_my_stuff.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Date;
import java.util.Map;

//import model.States;
import com.example.mcw0805.wheres_my_stuff.Model.ItemType;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Controller for submitting the lost item.
 *
 * @author Melanie Hall, Chaewon Min, Ted Shang
 */
public class SubmitFormActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    public final static String TAG = "SubmitFormActivity";

    /*
        Widgets for the item form page.
     */
    private EditText titleField;
    private EditText descriptField;
    private EditText latField;
    private EditText longField;
    private EditText rewardField;
    private TextView dollar;
    private Spinner categorySpinner;
    //private Spinner stateSpinner;
    private Spinner typeSpinner;
    private Button backButton;
    private Button postButton;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_submission_form);

        //instantiate widgets
        titleField = (EditText) findViewById(R.id.title_L);
        descriptField = (EditText) findViewById(R.id.description_L);
        latField = (EditText) findViewById(R.id.latitude_L);
        longField = (EditText) findViewById(R.id.longitude_L);
        rewardField = (EditText) findViewById(R.id.reward_L);
        dollar = (TextView) findViewById(R.id.dollar_L);
        categorySpinner = (Spinner) findViewById(R.id.category_Lspinner);
        //stateSpinner = (Spinner) findViewById(R.id.state_Lspinner);
        typeSpinner = (Spinner) findViewById(R.id.type_Lspinner);
        postButton = (Button) findViewById(R.id.postButton_L);
        backButton = (Button) findViewById(R.id.backButton_L);

//        ArrayAdapter<States> state_Adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item, States.values());
//        state_Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        stateSpinner.setAdapter(state_Adapter);

        ArrayAdapter<ItemCategory> category_Adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, ItemCategory.values());
        category_Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(category_Adapter);

        ArrayAdapter<ItemType> type_Adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, ItemType.values());
        type_Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(type_Adapter);


        //reward field is visible only if LOST is selected from the typeSpinner
        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).equals(ItemType.FOUND) || parent.getItemAtPosition(position).equals(ItemType.NEED)) {
                    rewardField.setVisibility(View.INVISIBLE);
                    dollar.setVisibility(View.INVISIBLE);
                } else if (parent.getItemAtPosition(position).equals(ItemType.LOST)) {
                    rewardField.setVisibility(View.VISIBLE);
                    dollar.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //set listener for the buttons
        postButton.setOnClickListener(this);
        backButton.setOnClickListener(this);

        //set Firebase authorization and get current user who is logged in
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
            setFieldVars();

            //get current time
            long currentTime = System.currentTimeMillis();
            Date dateTime = new Date(currentTime);

            if (inputItemType == ItemType.LOST) {
                submitLostItem(dateTime);
            } else if (inputItemType == ItemType.FOUND) {
                submitFoundItem(dateTime);
            }

            Toast.makeText(this, "Post Added!", Toast.LENGTH_LONG).show();
            Intent submitPostIntent = new Intent(SubmitFormActivity.this, Dashboard.class);

            startActivity(submitPostIntent);
            finish();
        }

        if (v == backButton) {
            startActivity(new Intent(this, Dashboard.class));
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
     *
     * @param dateTime current date-time
     */
    private void submitLostItem(Date dateTime) {

        int reward = Integer.parseInt(rewardField.getText().toString());

        newItem = new LostItem(inputName, inputDescription, dateTime,
                inputLongitude, inputLatitude, inputItemCategory, uid, reward);
        incrementSubmissionCount();
        newItem.writeToDatabase(LostItem.getChildRef());


    }

    /**
     * Method that submits found item information to the database.
     *
     * @param dateTime current date-time
     */
    private void submitFoundItem(Date dateTime) {
        newItem = new FoundItem(inputName, inputDescription, dateTime,
                inputLongitude, inputLatitude, inputItemCategory, uid);
        incrementSubmissionCount();
        newItem.writeToDatabase(FoundItem.getChildRef());
    }

    /**
     * Initializes the various texts/numbers from the form as instance data
     * to be used to create Item objects.
     */
    private void setFieldVars() {
        inputName = titleField.getText().toString();
        inputDescription = descriptField.getText().toString();

        try {
            inputLatitude = Double.parseDouble(latField.getText().toString());
            inputLongitude = Double.parseDouble(longField.getText().toString());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Enter Valid latitude and longitude");
        }

        inputItemCategory = (ItemCategory) categorySpinner.getSelectedItem();
        inputItemType = (ItemType) typeSpinner.getSelectedItem();

        uid = firebaseUser.getUid();
    }

    /**
     * Gets the current user from the database and increments the count
     * of number of items posted.
     */
    private void incrementSubmissionCount() {
        final DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users/" + this.uid );
        final DatabaseReference itemCountRef = FirebaseDatabase.getInstance().getReference("users/" + this.uid + "/itemCount");

        //final int[] uniqueNumber = {0};

        userRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String key = dataSnapshot.getKey();
                String val = dataSnapshot.getValue().toString();

                if (key.equals("itemCount")) {
                    Integer count = Integer.parseInt(String.valueOf(val));
                    Log.d(TAG, "Before incrementing- " + key + ": " + count);

                    count++;
                    Log.d(TAG, "After incrementing- " + key + ": " + count);

                    itemCountRef.setValue(count);

                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }


}
