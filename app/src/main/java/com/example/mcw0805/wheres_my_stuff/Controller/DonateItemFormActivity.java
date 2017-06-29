package com.example.mcw0805.wheres_my_stuff.Controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mcw0805.wheres_my_stuff.R;

public class DonateItemFormActivity extends AppCompatActivity implements View.OnClickListener {

    private Button postButton;
    private Button cancelButton;
    private EditText title_R;
    private EditText descript_R;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate_item_form);

        title_R = (EditText) findViewById(R.id.title_R);
        descript_R = (EditText) findViewById(R.id.description_R);
        postButton = (Button) findViewById(R.id.post_R);
        cancelButton = (Button) findViewById(R.id.cancel_R);

        postButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == postButton) {
            //submitLostItem();
            Toast.makeText(this, "Post Added!", Toast.LENGTH_LONG).show();
            finish();
        }

        if (v == cancelButton) {
            startActivity(new Intent(this, Dashboard.class));
        }
    }
}
