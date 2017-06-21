package com.example.mcw0805.wheres_my_stuff.Controller;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.mcw0805.wheres_my_stuff.R;

import model.Categories;
import model.States;
import model.Type;
import model.Status;

public class LostItemFormActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private EditText titleField;
    private EditText descriptField;
    private EditText cityField;
    private EditText zipField;
    private EditText rewardField;
    private Spinner statSpinner;
    private Spinner categorySpinner;
    private Spinner stateSpinner;
    private Spinner typeSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_item_form);

        titleField = (EditText) findViewById(R.id.title_L);
        descriptField = (EditText) findViewById(R.id.description_L);
        cityField = (EditText) findViewById(R.id.city_L);
        zipField = (EditText) findViewById(R.id.zipcode_L);
        rewardField = (EditText) findViewById(R.id.reward_L);
        statSpinner = (Spinner) findViewById(R.id.status_Lspinner);
        categorySpinner = (Spinner) findViewById(R.id.category_Lspinner);
        stateSpinner = (Spinner) findViewById(R.id.state_Lspinner);
        typeSpinner = (Spinner) findViewById(R.id.type_Lspinner);

        ArrayAdapter<States> state_Adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item, States.values());
        state_Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        stateSpinner.setAdapter(state_Adapter);

        ArrayAdapter<Categories> category_Adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item, Categories.values());
        category_Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(category_Adapter);

        ArrayAdapter<Type> type_Adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item, Type.values());
        type_Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(type_Adapter);

        ArrayAdapter<Status> status_Adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item, Status.values());
        status_Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statSpinner.setAdapter(status_Adapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
