package com.example.mcw0805.wheres_my_stuff.Controller;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.mcw0805.wheres_my_stuff.R;

/**
 * Created by Chianne on 7/9/17.
 */


public class Dash_Bottom_Fragment extends Fragment{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_dashboard_bottom, container, false);
    }
}