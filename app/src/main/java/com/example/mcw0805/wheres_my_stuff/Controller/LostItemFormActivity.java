package com.example.mcw0805.wheres_my_stuff.Controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.mcw0805.wheres_my_stuff.Model.Item;
import com.example.mcw0805.wheres_my_stuff.Model.ItemCategory;
import com.example.mcw0805.wheres_my_stuff.Model.LostItem;
import com.example.mcw0805.wheres_my_stuff.Model.User;
import com.example.mcw0805.wheres_my_stuff.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

import model.Categories;
//import model.States;
import model.Type;

public class LostItemFormActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    private EditText titleField;
    private EditText descriptField;
    private EditText latField;
    private EditText longField;
    private EditText rewardField;
    private Spinner categorySpinner;
    //private Spinner stateSpinner;
    private Spinner typeSpinner;
    private Button backButton;
    private Button postButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_item_form);

        titleField = (EditText) findViewById(R.id.title_L);
        descriptField = (EditText) findViewById(R.id.description_L);
        latField = (EditText) findViewById(R.id.latitude_L);
        longField = (EditText) findViewById(R.id.longitude_L);
        rewardField = (EditText) findViewById(R.id.reward_L);
        categorySpinner = (Spinner) findViewById(R.id.category_Lspinner);
        //stateSpinner = (Spinner) findViewById(R.id.state_Lspinner);
        typeSpinner = (Spinner) findViewById(R.id.type_Lspinner);
        postButton = (Button) findViewById(R.id.postButton_L);
        backButton = (Button) findViewById(R.id.backButton_L);

//        ArrayAdapter<States> state_Adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item, States.values());
//        state_Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        stateSpinner.setAdapter(state_Adapter);

        ArrayAdapter<Categories> category_Adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item, Categories.values());
        category_Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(category_Adapter);

        ArrayAdapter<Type> type_Adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item, Type.values());
        type_Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(type_Adapter);

        postButton.setOnClickListener(this);
        backButton.setOnClickListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {
        if (v == postButton) {
            //Create the item
            String name = titleField.getText().toString();
            String description = descriptField.getText().toString();
            double lat = Double.parseDouble(latField.getText().toString());
            double longit = Double.parseDouble(longField.getText().toString());
            int reward = Integer.parseInt(rewardField.getText().toString());
            String itemType = typeSpinner.getSelectedItem().toString();
            String itemCat =categorySpinner.getSelectedItem().toString();
            Date date = new Date();
            User user = new User("test", "email");
            boolean status = false;
            Item k = new LostItem(name, description, date, longit, lat, itemCat, user, status, reward);
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            //traverse through database here
            DatabaseReference myRef = database.getReference("posts/lost-items/123456789");
            //add to k object to database
            myRef.setValue(k);
            Toast.makeText(this, "Post Added!", Toast.LENGTH_LONG).show();
            finish();
            Intent intent = new Intent(LostItemFormActivity.this, Dashboard.class);
            LostItemFormActivity.this.startActivity(intent);
        }

        if (v == backButton) {
            startActivity(new Intent(this, Dashboard.class));
        }
    }
}
