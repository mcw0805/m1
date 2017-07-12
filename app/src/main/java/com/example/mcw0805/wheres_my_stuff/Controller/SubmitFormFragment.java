package com.example.mcw0805.wheres_my_stuff.Controller;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.mcw0805.wheres_my_stuff.Model.ItemType;
import com.example.mcw0805.wheres_my_stuff.Model.LostItem;
import com.example.mcw0805.wheres_my_stuff.Model.User;
import com.example.mcw0805.wheres_my_stuff.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//import com.example.mcw0805.wheres_my_stuff.Model.States;

/**
 * Controller for submitting the lost item.
 *
 * @author Melanie Hall, Chaewon Min, Ted Shang
 */
public class SubmitFormFragment extends Fragment
        implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    public final static String TAG = "SubmitFormFragment";

    /*
        Widgets for the item form page.
     */
    private EditText titleField;
    private EditText descriptField;
    private EditText latField;
    private EditText longField;
    private EditText rewardField;
    private TextView dollar;
    private TextView category;
    private Spinner categorySpinner;
    private Spinner typeSpinner;
    private Button backButton;
    private Button postButton;
    //private Spinner stateSpinner;


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
    private String name;
    private double inputLatitude;
    private double inputLongitude;
    private ItemCategory inputItemCategory;
    private ItemType inputItemType;

    private List<User> users;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                         Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.item_submission_form,container, false);

        users = new ArrayList<>();

        //instantiate widgets
        titleField = (EditText) view.findViewById(R.id.title_L);
        descriptField = (EditText) view.findViewById(R.id.description_L);
        latField = (EditText) view.findViewById(R.id.latitude_L);
        longField = (EditText) view.findViewById(R.id.longitude_L);
        rewardField = (EditText) view.findViewById(R.id.reward_L);
        dollar = (TextView) view.findViewById(R.id.dollar_L);
        category = (TextView) view.findViewById(R.id.category_L);
        categorySpinner = (Spinner) view.findViewById(R.id.category_Lspinner);
        //stateSpinner = (Spinner) findViewById(R.id.state_Lspinner);
        typeSpinner = (Spinner) view.findViewById(R.id.type_Lspinner);
        postButton = (Button) view.findViewById(R.id.postButton_L);
        backButton = (Button) view.findViewById(R.id.backButton_L);

//        ArrayAdapter<States> state_Adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item, States.values());
//        state_Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        stateSpinner.setAdapter(state_Adapter);

        ArrayAdapter<ItemCategory> category_Adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, ItemCategory.values());
        category_Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(category_Adapter);

        ArrayAdapter<ItemType> type_Adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, ItemType.values());
        type_Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(type_Adapter);


        //reward field is visible only if LOST is selected from the typeSpinner
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
                } else if (parent.getItemAtPosition(position).equals(ItemType.NEED)) { //need
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

        //set listener for the buttons
        postButton.setOnClickListener(this);
        backButton.setOnClickListener(this);

        //set Firebase authorization and get current user who is logged in
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
        assert firebaseUser != null;

        return view;
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
                AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
                builder1.setMessage("Invalid");
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
            Date dateTime = new Date(currentTime);

            if (inputItemType == ItemType.LOST) {
                submitLostItem(dateTime);
            } else if (inputItemType == ItemType.FOUND) {
                submitFoundItem(dateTime);
            }

            Toast.makeText(getActivity(), "Post Added!", Toast.LENGTH_LONG).show();
            Intent submitPostIntent = new Intent(getActivity(), Dashboard.class);
            //SubmitFormFragment.this --> after 'new Intent'
            startActivity(submitPostIntent);
            getActivity().finish();
        }

        if (v == backButton) {
            startActivity(new Intent(getActivity(), Dashboard.class));
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
        DatabaseReference child = LostItem.getLostItemsRef().child(uid + "--" + LostItem.getLostItemsRef().push().getKey());
        //newItem.writeToDatabase(LostItem.getChildRef());
        newItem.writeToDatabase(child);

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


        Boolean x = users.isEmpty();         //delete this
        Log.d("SUBMIT", x.toString());        //delete this

        DatabaseReference child = FoundItem.getFoundItemsRef().child(uid + "--" + FoundItem.getFoundItemsRef().push().getKey());
        //newItem.writeToDatabase(FoundItem.getChildRef());
        newItem.writeToDatabase(child);
    }

    /**
     * Initializes the various texts/numbers from the form as instance data
     * to be used to create Item objects.
     */
    private boolean setFieldVars() {

        boolean valid = true;

        if (FormValidation.textEmpty(new EditText[] {titleField, descriptField })) {
            valid = false;
        }

        inputName = titleField.getText().toString();
        inputDescription = descriptField.getText().toString();

        inputItemType = (ItemType) typeSpinner.getSelectedItem();

        inputItemCategory = (ItemCategory) categorySpinner.getSelectedItem();
        if (FormValidation.categoryNothingSelected(inputItemCategory)
                || typeSpinner.getVisibility() == View.INVISIBLE) {
            valid = false;
            if (!valid) {
                Log.w(TAG, "Item category: Nothing selected");
            }
        }

        try {
            inputLatitude = Double.parseDouble(latField.getText().toString());
            inputLongitude = Double.parseDouble(longField.getText().toString());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Enter Valid latitude and longitude");
        }


        uid = firebaseUser.getUid();


        if (rewardField.getVisibility() == View.VISIBLE && !FormValidation.isValidInteger(rewardField)) {
                valid = false;

            if (!valid) {
                Log.w(TAG, "Reward must be an integer and cannot be empty.");
            }

        }

        return valid;
    }

    /**
     * Gets the current user from the database and increments the count
     * of number of items posted.
     */
    private void incrementSubmissionCount() {
        final DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users/" + this.uid );
        final DatabaseReference itemCountRef = userRef.child("itemCount");

        userRef.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String key = dataSnapshot.getKey();
                String val = dataSnapshot.getValue().toString();

                if (key.equals("itemCount")) {
                    Integer count = Integer.parseInt(String.valueOf(val));
                    Log.i(TAG, "Before incrementing- " + key + ": " + count);

                    count++;
                    Log.i(TAG, "After incrementing- " + key + ": " + count);

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
