package com.example.mcw0805.wheres_my_stuff.Controller;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import com.example.mcw0805.wheres_my_stuff.R;

public class LostItemList extends AppCompatActivity {

    ArrayAdapter lostItem_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_item_list);
    }
}
