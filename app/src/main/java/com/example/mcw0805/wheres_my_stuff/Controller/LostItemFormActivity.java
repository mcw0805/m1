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

import com.example.mcw0805.wheres_my_stuff.Model.Item;
import com.example.mcw0805.wheres_my_stuff.Model.ItemCategory;
import com.example.mcw0805.wheres_my_stuff.Model.LostItem;
import com.example.mcw0805.wheres_my_stuff.Model.User;
import com.example.mcw0805.wheres_my_stuff.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.Date;

import model.Categories;
//import model.States;
import model.Type;

/**
 * Controller for submitting the lost item.
 *
 * @author Melanie Hall, Chaewon Min, Ted Shang
 */
public class LostItemFormActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    /*
       Instance variables for the lost item form page.
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

    private LostItem newLostItem;

    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;

    String inputName;
    String inputDescription;
    String uid;
    double inputLatitude;
    double inputLongitude;
    int reward;
    ItemCategory inputItemCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_item_form);

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

        ArrayAdapter<Categories> category_Adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, ItemCategory.values());
        category_Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(category_Adapter);

        ArrayAdapter<Type> type_Adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, Type.values());
        type_Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(type_Adapter);

        postButton.setOnClickListener(this);
        backButton.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
        assert firebaseUser != null;

        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected (AdapterView < ? > parent, View view,int position, long id){
                if (parent.getItemAtPosition(position).equals(Type.FOUND) || parent.getItemAtPosition(position).equals(Type.NEED)) {
                    rewardField.setVisibility(View.INVISIBLE);
                    dollar.setVisibility(View.INVISIBLE);
                } else if (parent.getItemAtPosition(position).equals(Type.LOST)) {
                    rewardField.setVisibility(View.VISIBLE);
                    dollar.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected (AdapterView<?> parent){
            }
        });
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
            submitLostItem();
            Toast.makeText(this, "Post Added!", Toast.LENGTH_LONG).show();
            finish();
        }

        if (v == backButton) {
            startActivity(new Intent(this, Dashboard.class));
        }
    }

    /**
     * Method that submits lost item information to the database.
     */
    private void submitLostItem() {
        inputName = titleField.getText().toString();
        inputDescription = descriptField.getText().toString();

        try {
            inputLatitude = Double.parseDouble(latField.getText().toString());
            inputLongitude = Double.parseDouble(longField.getText().toString());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Enter Valid latitude and longitude");
        }

        reward = Integer.parseInt(rewardField.getText().toString());
        String itemType = typeSpinner.getSelectedItem().toString();
        inputItemCategory = (ItemCategory) categorySpinner.getSelectedItem();

        long currentTime = System.currentTimeMillis();
        Date date = new Date(currentTime);

        uid = firebaseUser.getUid();

        newLostItem = new LostItem(inputName, inputDescription, date,
                inputLongitude, inputLatitude, inputItemCategory, uid, reward);
        newLostItem.writeToDatabase();
        Intent submitPostIntent = new Intent(LostItemFormActivity.this, Dashboard.class);
        startActivity(submitPostIntent);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
