package com.example.mcw0805.wheres_my_stuff.Controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mcw0805.wheres_my_stuff.R;

public class DonateItemFormFragment extends Fragment implements View.OnClickListener {

    public final static String TAG = "DonateItemFormFragment";

    private Button postButton;
    private Button cancelButton;
    private EditText title_R;
    private EditText descript_R;

    public View onCreate(LayoutInflater inflater, ViewGroup container,
                         Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_donate_item_form,container, false);

        title_R = (EditText) getView().findViewById(R.id.title_R);
        descript_R = (EditText) getView().findViewById(R.id.description_R);
        postButton = (Button) getView().findViewById(R.id.post_R);
        cancelButton = (Button) getView().findViewById(R.id.cancel_R);

        postButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        if (v == postButton) {
            //submitLostItem();
            Toast.makeText(getActivity(), "Post Added!", Toast.LENGTH_LONG).show();
            getActivity().finish();
        }

        if (v == cancelButton) {
            startActivity(new Intent(getActivity(), Dashboard.class));
        }
    }
}
